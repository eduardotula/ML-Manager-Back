package org.florense.outbound.port.mercadolivre;

import jakarta.resource.spi.IllegalStateException;
import org.florense.domain.model.Order;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.time.LocalDateTime;
import java.util.List;

public interface MercadoLivreVendaPort {

    Order getOrder(String mlOrderId, User user, boolean retry) throws MercadoLivreException, FailRequestRefreshTokenException;

    List<Order> listAllordersByDate(User user, List<MLStatusEnum> status, LocalDateTime startDate, LocalDateTime endDate, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException;

    List<Order> listOrdersUntilExistent(List<MLStatusEnum> status, Long existentOrderId, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException;
}
