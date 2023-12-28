package org.florense.inbound.port;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.florense.domain.model.Anuncio;
import org.florense.inbound.adapter.dto.AnuncioDto;
import org.florense.inbound.adapter.dto.AnuncioDtoSimple;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;

public interface AnuncioAdapterPort {


    Anuncio createUpdate(Anuncio anuncio);

    //Cria e atualiza com mercado livre
    Anuncio createMlSearch(Anuncio anuncio) throws FailRequestRefreshTokenException;

    //Somente atualiza um anuncio
    Anuncio updateSimple(Anuncio anuncio);

    //Atualiza dados somente do mercado livre
    Anuncio updateSearch(String mlId) throws FailRequestRefreshTokenException;

    List<String> listAllActiveMl() throws FailRequestRefreshTokenException;

    List<String> listAllActiveMlMinusRegistered() throws FailRequestRefreshTokenException;

    Anuncio findAnuncioByMlId(String mlId);

    @Transactional
    Anuncio findAnuncioByMlIdSearch(String mlId) throws FailRequestRefreshTokenException;

    List<Anuncio> listAll();

    void deleteBy(Long id);
}
