package org.florense.domain.scheduler.jobs.checkorderstatuschange;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.domain.model.User;
import org.florense.domain.model.Venda;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class CheckOrderStatusChange {

    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;
    @Inject
    Logger logger;

    public void execute(User user){
        try {
            List<MLStatusEnum> statusEnums = Arrays.asList(MLStatusEnum.CANCELLED);
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusMonths(3);
            List<Order> orders = mercadoLivreVendaPort.listAllordersByDate(user, statusEnums, startDate, endDate,true);
            List<Order> updatedOrders = new LinkedList<>();
            for(Order mlOrder: orders){
                Order existingOrder = orderEntityPort.findByOrderId(mlOrder.getOrderId());

                if(Objects.nonNull(existingOrder)){
                    for(Venda mlVenda : mlOrder.getVendas()){
                        //Procura pelo mesmo produto na ordem
                        List<Venda> vendas = existingOrder.getVendas().stream().filter(existVenda -> existVenda.getAnuncio().
                                getMlId().equalsIgnoreCase(mlVenda.getAnuncio().getMlId())).collect(Collectors.toList());
                        if(!vendas.isEmpty()){
                            vendas.get(0).setStatus(mlVenda.getStatus());
                            updatedOrders.add(existingOrder);
                        }
                    }
                }
            }

            orderEntityPort.createUpdateAll(updatedOrders);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new IllegalStateException(e);
        }
    }
}
