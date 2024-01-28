package org.florense.outbound.port.mercadolivre;

import jakarta.resource.spi.IllegalStateException;
import org.florense.domain.model.Order;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;

public interface MercadoLivreVendaPort {

    List<Order> listAllOrders(User user, boolean retry) throws FailRequestRefreshTokenException, IllegalStateException;

    List<Order> listOrdersUntilExistent(List<MLStatusEnum> status, Long existentOrderId, User user, boolean retry) throws FailRequestRefreshTokenException, IllegalStateException;
}
