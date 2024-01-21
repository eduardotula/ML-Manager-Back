package org.florense.outbound.port.postgre;

import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.User;
import org.florense.domain.model.filters.OrderFilter;

import java.util.List;

public interface OrderEntityPort {
    Order createUpdate(Order order);

    List<Order> createUpdateAll(List<Order> orderList);

    Pagination<Order> listByFilters(OrderFilter filter, PageParam pageParam);

    Order getLastOrderByUser(User user);

    Order findById(Long id);

    Order findByOrderId(Long orderId);

    void deleteById(Long id);
}
