package org.florense.outbound.port.postgre;

import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;

import java.util.List;

public interface AnuncioEntityPort {
    Anuncio createUpdate(Anuncio anuncio);

    Anuncio findById(Long id);
    Anuncio findAnyById(Long id);

    Anuncio findByMlId(String mlId, User user);
    Anuncio findAnyByMlId(String mlId, User user);

    List<Anuncio> listAll(User user);

    List<Anuncio> listAllRegistered(User user);

    void disableById(Long id);

    void deleteById(Long id);
}
