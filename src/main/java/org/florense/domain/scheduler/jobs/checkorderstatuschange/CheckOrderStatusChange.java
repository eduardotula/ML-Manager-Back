package org.florense.domain.scheduler.jobs.checkorderstatuschange;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.domain.model.User;
import org.florense.domain.model.Venda;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.OrderEntityPort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class CheckOrderStatusChange {

    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;

    public void execute(User user){
        try {
            List<MLStatusEnum> statusEnums = Arrays.asList(MLStatusEnum.CANCELLED);
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusMonths(3);
            List<Order> orders = mercadoLivreVendaPort.listAllordersByDate(user, statusEnums, startDate, endDate,true);

            for(Order mlOrder: orders){
                Order existingOrder = orderEntityPort.findByOrderId(mlOrder.getOrderId());

                if(Objects.nonNull(existingOrder)){
                    for(Venda mlVenda : mlOrder.getVendas()){
                        //Procura pelo mesmo produto na ordem
                        List<Venda> vendas = existingOrder.getVendas().stream().filter(existVenda -> existVenda.getAnuncio().
                                getMlId().equalsIgnoreCase(mlVenda.getAnuncio().getMlId())).collect(Collectors.toList());
                        if(!vendas.isEmpty()) vendas.get(0).setStatus(mlVenda.getStatus());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
