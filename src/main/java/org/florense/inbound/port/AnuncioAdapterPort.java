package org.florense.inbound.port;

import jakarta.resource.spi.IllegalStateException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.florense.domain.model.Anuncio;
import org.florense.inbound.adapter.dto.AnuncioDto;
import org.florense.inbound.adapter.dto.AnuncioDtoSimple;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;

public interface AnuncioAdapterPort {


    @Transactional
    Anuncio createUpdate(Anuncio anuncio, Long userId);

    //Cria e atualiza com mercado livre
    @Transactional
    Anuncio createMlSearch(Anuncio anuncio, Long userId) throws FailRequestRefreshTokenException, IllegalStateException;

    //Somente atualiza um anuncio
    @Transactional
    Anuncio updateSimple(Anuncio anuncio, Long userId);

    //Atualiza dados somente do mercado livre
    @Transactional
    Anuncio updateSearch(String mlId, Long userId) throws FailRequestRefreshTokenException, IllegalStateException;

    @Transactional
    List<String> listAllActiveMl(Long userId) throws FailRequestRefreshTokenException;

    @Transactional
    List<String> listAllActiveMlMinusRegistered(Long userId) throws FailRequestRefreshTokenException;

    @Transactional
    Anuncio findAnuncioByMlId(String mlId, Long userId);

    @Transactional
    Anuncio findAnyAnuncioByMlId(String mlId, Long userId);

    @Transactional
    Anuncio findAnuncioByMlIdSearch(String mlId, Long userId) throws FailRequestRefreshTokenException, IllegalStateException;

    @Transactional
    List<Anuncio> listAll(Long userId);

    @Transactional
    List<Anuncio> listAllRegistered(Long userId);

    @Transactional
    void deleteBy(Long id) throws IllegalStateException;
}
