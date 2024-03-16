package org.florense.domain.scheduler.jobs.listallneworders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.*;
import org.florense.outbound.adapter.mercadolivre.exceptions.MLErrorTypesEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.port.mercadolivre.MercadoLivreAnuncioPort;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ListAllNewOrders {

    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;

    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    AnuncioEntityPort anuncioEntityPort;
    @Inject
    MercadoLivreAnuncioPort mercadoLivreAnuncioPort;


    public void execute(User user) throws IllegalStateException{
        try {
            Order lastOrder = getLastOrderByUser(user);
            List<Order> orders;
            List<MLStatusEnum> statusEnums = Arrays.asList(MLStatusEnum.PAID, MLStatusEnum.CANCELLED);

            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = LocalDateTime.of(1990,1,1,1,0);
            if (lastOrder != null) orders = mercadoLivreVendaPort.listOrdersUntilExistent(statusEnums, lastOrder.getOrderId(), user, true);
            else orders = mercadoLivreVendaPort.listAllordersByDate(user, statusEnums, startDate, endDate,true);

            List<Order> returnOrders = new LinkedList<>();
            for (Order order : orders) {

                //Procura se o mesmo pedido já existe e prepara para atualizar
                var existingOrder = findByOrderId(order.getOrderId());
                if (Objects.nonNull(existingOrder)) {
                    order.setId(existingOrder.getId());
                    order.updateVendasByMatchingByMlId(existingOrder.getVendas());
                }
                //Reliza a atualização de um pedido ou cria, caso produto não existir é criado um produto temporario
                for (Venda venda : order.getVendas()) {

                    double valorFrete = mercadoLivreAnuncioPort.getFrete(order.getShippingId(),user,true);
                    venda.setCustoFrete(valorFrete);
                    var existingAnuncio = findAnuncioByMlId(venda.getAnuncio().getMlId(), user.getId());

                    if (Objects.nonNull(existingAnuncio)) {
                        venda.setAnuncio(existingAnuncio);
                    }else{
                        Anuncio anuncio = venda.getAnuncio();
                        anuncio.setComplete(false);
                        anuncio.setCustoFrete(valorFrete);
                        venda.setAnuncio(createAnuncio(anuncio));
                    }

                    venda.setDataFromAnuncio(venda.getAnuncio());
                    returnOrders.add(order);
                }
            }
            orderEntityPort.createUpdateAll(returnOrders);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Anuncio createAnuncio(Anuncio anuncio){
        return anuncioEntityPort.createUpdate(anuncio);
    }

    public Order getLastOrderByUser(User user){
        return orderEntityPort.getLastOrderByUser(user);
    }

    public Order findByOrderId(Long orderId){
        return orderEntityPort.findByOrderId(orderId);
    }

    public Anuncio findAnuncioByMlId(String mlId, long userId){
        return anuncioEntityPort.findAnyByMlId(mlId, User.builder().id(userId).build());
    }


}
