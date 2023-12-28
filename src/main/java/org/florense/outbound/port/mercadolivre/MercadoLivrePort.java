package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Anuncio;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;

import java.util.List;

public interface MercadoLivrePort {

    Anuncio getAnuncio(String mlId, boolean retry) throws FailRequestRefreshTokenException;

    Double getTarifas(Double preco, String categoria, boolean retry) throws FailRequestRefreshTokenException;

    Double getFrete(String mlId, String cep, boolean retry) throws FailRequestRefreshTokenException;

    List<String> listActiveMlIds(boolean retry) throws FailRequestRefreshTokenException;
}
