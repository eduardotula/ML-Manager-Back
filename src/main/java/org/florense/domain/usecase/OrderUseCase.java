package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestScoped
public class OrderUseCase {

    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;
    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    AnuncioEntityPort anuncioEntityPort;

    /*
        Realiza a busca por novas Orders Mercado Livre, cria ou atualiza as Orders mas somente aquelas que
        já existir um Anuncio do produto contido na ordem
     */
    public List<Order> searchAllOrders() throws FailRequestRefreshTokenException {
        List<Order> orders = mercadoLivreVendaPort.listAllVendas(true);

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

    public void deleteById(Long id){
        if(Objects.isNull(orderEntityPort.findById(id))) throw new IllegalArgumentException(String.format("Order com id: %s não encontrado",id));
        orderEntityPort.deleteById(id);
    }

}
