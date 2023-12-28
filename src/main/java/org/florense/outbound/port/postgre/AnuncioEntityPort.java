package org.florense.outbound.port.postgre;

import org.florense.domain.model.Anuncio;

import java.util.List;

public interface AnuncioEntityPort {
    Anuncio saveUpdate(Anuncio anuncio);

    Anuncio findById(Long id);

    Anuncio findByMlId(String mlId);

    List<Anuncio> listAll();

    void deleteById(Long id);
}
