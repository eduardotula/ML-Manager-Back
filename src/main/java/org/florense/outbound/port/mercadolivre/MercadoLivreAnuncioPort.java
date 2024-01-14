package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.domain.model.ListingTypeEnum;

import java.util.List;

public interface MercadoLivreAnuncioPort {


    Anuncio getAnuncio(String mlId, User user, boolean retry) throws FailRequestRefreshTokenException;

    Double getTarifas(Double preco, String categoria, ListingTypeEnum typeEnum, User user, boolean retry) throws FailRequestRefreshTokenException;

    Double getFrete(String mlId, String cep, User user, boolean retry) throws FailRequestRefreshTokenException;

    List<String> listActiveMlIds(User user, boolean retry) throws FailRequestRefreshTokenException;
}
