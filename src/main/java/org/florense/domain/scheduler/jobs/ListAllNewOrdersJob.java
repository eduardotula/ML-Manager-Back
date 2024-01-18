package org.florense.domain.scheduler.jobs;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Order;
import org.florense.domain.model.ScheduleJob;
import org.florense.domain.model.User;
import org.florense.domain.usecase.AnuncioUseCase;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.domain.util.OrderScheduelerJobKeyGenerator;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;
import org.hibernate.validator.constraints.URL;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListAllNewOrdersJob implements Job {

    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;
    @Inject
    SchedulerJobEntityPort schedulerJobEntityPort;
    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    AnuncioEntityPort anuncioEntityPort;

    @Inject
    OrderScheduelerJobKeyGenerator nameGenerator;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            User user = (User) jobExecutionContext.getJobDetail().getJobDataMap().get("User");
            Order lastOrder = getLastOrderByUser(user);
            List<Order> orders = null;
            if (lastOrder != null) {
                List<MLStatusEnum> statusEnums = Arrays.asList(MLStatusEnum.PAID, MLStatusEnum.CANCELLED);
                orders = mercadoLivreVendaPort.listVendasUntilExistent(statusEnums, lastOrder.getId(), user, true);
            } else {
                orders = mercadoLivreVendaPort.listAllVendas(user, true);
            }

            updateNextRunTime(jobExecutionContext, user);

            List<Order> returnOrders = new ArrayList<>();
            orders.forEach(order -> {

                //Procura se o mesmo pedido já existe e prepara para atualizar
                var existingOrder = findByOrderId(order.getOrderId());
                if (Objects.nonNull(existingOrder)) {
                    order.setId(existingOrder.getId());
                    order.updateVendasByMatchingByMlId(existingOrder.getVendas());
                }

                //Reliza a atualização de um pedido ou cria mas somente para anuncios já existentes
                order.getVendas().forEach(venda -> {
                    var existingAnuncio = findAnuncioByMlId(venda.getAnuncio().getMlId(), user.getId());
                    if (Objects.nonNull(existingAnuncio)) {
                        venda.setAnuncio(existingAnuncio);
                        venda.setDataFromAnuncio(venda.getAnuncio());
                        returnOrders.add(orderEntityPort.saveUpdate(order));
                    }
                });

            });

        } catch (FailRequestRefreshTokenException e) {
            throw new JobExecutionException(e);
        }
    }

    public Order getLastOrderByUser(User user){
        return orderEntityPort.getLastOrderByUser(user);
    }

    public Order findByOrderId(Long orderId){
        return orderEntityPort.findByOrderId(orderId);
    }

    public Anuncio findAnuncioByMlId(String mlId, long userId){
        return anuncioEntityPort.findByMlId(mlId);
    }

    private void updateNextRunTime(JobExecutionContext jobExecutionContext, User user) {
        ScheduleJob scheduleJob = schedulerJobEntityPort.findByJobName(nameGenerator.createJobKey(user).getName());
        if (scheduleJob != null) {
            scheduleJob.setNextRunTime(jobExecutionContext.
                    getNextFireTime().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
    }
}
