package org.florense.domain.scheduler.jobusecase;

import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.outbound.adapter.mercadolivre.MLStatusEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrderUseCaseJob implements Job {

    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;
    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    AnuncioEntityPort anuncioEntityPort;
//    /*
//        Realiza a busca por novas Orders Mercado Livre, cria ou atualiza as Orders mas somente aquelas que
//        já existir um Anuncio do produto contido na ordem
//     */
//    public List<Order> listAllOrders() throws FailRequestRefreshTokenException {
//        List<Order> orders = mercadoLivreVendaPort.listAllVendas(true);
//
//        List<Order> returnOrders = new ArrayList<>();
//        orders.forEach(order -> {
//
//            //Procura se o mesmo pedido já existe e prepara para atualizar
//            var existingOrder = orderEntityPort.findByOrderId(order.getOrderId());
//            if(Objects.nonNull(existingOrder)){
//                order.setId(existingOrder.getId());
//                order.updateVendasByMatchingByMlId(existingOrder.getVendas());
//            }
//
//            //Reliza a atualização de um pedido ou cria mas somente para anuncios já existentes
//            order.getVendas().forEach(venda -> {
//                var existingAnuncio = anuncioEntityPort.findByMlId(venda.getAnuncio().getMlId());
//                if(Objects.nonNull(existingAnuncio)){
//                    venda.setAnuncio(existingAnuncio);
//                    venda.setDataFromAnuncio(venda.getAnuncio());
//                    returnOrders.add(orderEntityPort.saveUpdate(order));
//                }
//            });
//
//        });
//
//        return returnOrders;
//    }

    /*
         Realiza a busca por novas Orders Mercado Livre, cria ou atualiza as Orders mas somente aquelas que
        já existir um Anuncio do produto contido na ordem
     */
    public List<Order> listAllNewOrders() throws FailRequestRefreshTokenException {
        Order lasOrder = orderEntityPort.getLastOrder();
        List<MLStatusEnum> statusEnums = Arrays.asList(MLStatusEnum.PAID, MLStatusEnum.CANCELLED);
        List<Order> orders = mercadoLivreVendaPort.listVendasUntilExistent(statusEnums, lasOrder.getId(), true);

        List<Order> returnOrders = new ArrayList<>();
        orders.forEach(order -> {

            //Procura se o mesmo pedido já existe e prepara para atualizar
            var existingOrder = orderEntityPort.findByOrderId(order.getOrderId());
            if(Objects.nonNull(existingOrder)){
                order.setId(existingOrder.getId());
                order.updateVendasByMatchingByMlId(existingOrder.getVendas());
            }

            //Reliza a atualização de um pedido ou cria mas somente para anuncios já existentes
            order.getVendas().forEach(venda -> {
                var existingAnuncio = anuncioEntityPort.findByMlId(venda.getAnuncio().getMlId());
                if(Objects.nonNull(existingAnuncio)){
                    venda.setAnuncio(existingAnuncio);
                    venda.setDataFromAnuncio(venda.getAnuncio());
                    returnOrders.add(orderEntityPort.saveUpdate(order));
                }
            });

        });
        return returnOrders;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
