package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Anuncio;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;

public interface MercadoLivreAnuncioPort {

    Anuncio getAnuncio(String mlId, boolean retry) throws FailRequestRefreshTokenException;

    Double getTarifas(Double preco, String categoria, boolean retry) throws FailRequestRefreshTokenException;

    Double getFrete(String mlId, String cep, boolean retry) throws FailRequestRefreshTokenException;

    List<String> listActiveMlIds(boolean retry) throws FailRequestRefreshTokenException;
}
