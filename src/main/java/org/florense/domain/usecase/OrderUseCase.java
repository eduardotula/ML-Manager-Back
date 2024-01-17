package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.User;
import org.florense.domain.model.filters.OrderFilter;
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
    OrderEntityPort orderEntityPort;

    @Transactional
    public Pagination<Order> listOrderByFilters(OrderFilter filter, PageParam pageParam){
        return orderEntityPort.listByFilters(filter, pageParam);
    }

    @Transactional
    public Order getLastOrderByUser(User user){
        return orderEntityPort.getLastOrderByUser(user);
    }

    @Transactional
    public void deleteById(Long id){
        if(Objects.isNull(orderEntityPort.findById(id))) throw new IllegalArgumentException(String.format("Order com id: %s não encontrado",id));
        orderEntityPort.deleteById(id);
    }

}
