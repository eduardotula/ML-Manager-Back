package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
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

    public List<Order> searchAllOrders() throws FailRequestRefreshTokenException {
        List<Order> orders = mercadoLivreVendaPort.listAllVendas(true);

        List<Order> returnOrders = new ArrayList<>();
        orders.forEach(order -> {
            var existingOrder = orderEntityPort.findByOrderId(order.getOrderId());
           // if(Objects.isNull(existingOrder)) returnOrders.add(orderEntityPort.saveUpdate(order));
        });

        return returnOrders;
    }

    public void deleteById(Long id){
        if(Objects.isNull(orderEntityPort.findById(id))) throw new IllegalArgumentException(String.format("Order com id: %s não encontrado",id));
        orderEntityPort.deleteById(id);
    }

}
