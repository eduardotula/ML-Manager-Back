package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.domain.model.enums.ListingTypeEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;

import java.util.List;

public interface MercadoLivreAnuncioPort {


    Anuncio getAnuncio(String mlId, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException;

    double getTarifas(double preco, String categoria, ListingTypeEnum typeEnum, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException;

    double getFrete(String mlId,String anuncioStatus,User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException;

    double getFrete(Long shippingId, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException;

    List<String> listAllAnunciosMercadoLivre(User user, boolean isActive, boolean retry) throws FailRequestRefreshTokenException;
}
