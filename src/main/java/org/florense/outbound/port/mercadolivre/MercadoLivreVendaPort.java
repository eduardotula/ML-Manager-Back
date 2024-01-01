package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Order;
import org.florense.outbound.adapter.mercadolivre.MLStatusEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;

public interface MercadoLivreVendaPort {
    List<Order> listAllVendas(boolean retry) throws FailRequestRefreshTokenException;

    List<Order> listVendasUntilExistent(List<MLStatusEnum> status, Long existentOrderId, boolean retry) throws FailRequestRefreshTokenException;
}
