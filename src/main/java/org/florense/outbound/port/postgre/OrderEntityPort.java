package org.florense.outbound.port.postgre;

import org.florense.domain.model.*;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.outbound.adapter.postgre.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderEntityPort {
    Order createUpdate(Order order);

    List<Order> createUpdateAll(List<Order> orderList);

    void executeBeforeSave(OrderEntity orderEntity, LocalDateTime now);

    Pagination<Order> listByFilters(Long userId, OrderFilter filter);

    Order getLastOrderByUser(User user);

    Order findById(Long id);

    Order findByOrderId(Long orderId);

    void deleteById(Long id);

    List<Order> listAllOrdersByAnuncio(Anuncio anuncio);
}
