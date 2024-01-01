package org.florense.outbound.port.postgre;

import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.filters.OrderFilter;

public interface OrderEntityPort {
    Order saveUpdate(Order order);

    Pagination<Order> listByFilters(OrderFilter filter, PageParam pageParam);

    Order getLastOrder();

    Order findById(Long id);

    Order findByOrderId(Long orderId);

    void deleteById(Long id);
}
