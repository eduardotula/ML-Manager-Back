package org.florense.outbound.port.postgre;

import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;

import java.util.List;

public interface AnuncioEntityPort {
    Anuncio createUpdate(Anuncio anuncio);

    List<Anuncio> createUpdateAll(List<Anuncio> anuncios);

    void executeBeforeSave(AnuncioEntity anuncio);

    Anuncio findById(Long id);
    Anuncio findAnyById(Long id);

    Anuncio findByMlId(String mlId, User user);
    Anuncio findAnyByMlId(String mlId, User user);

    List<Anuncio> listAll(User user);

    List<Anuncio> listAllRegistered(User user);

    void disableById(Long id);

    void deleteById(Long id);
}
