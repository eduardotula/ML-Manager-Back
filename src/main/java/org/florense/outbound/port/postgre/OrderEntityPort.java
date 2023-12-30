package org.florense.outbound.port.postgre;

import org.florense.domain.model.Order;

public interface OrderEntityPort {
    Order saveUpdate(Order order);

    Order findById(Long id);

    Order findByOrderId(Long orderId);

    void deleteById(Long id);
}
