package org.florense.outbound.port.postgre;

import org.florense.domain.model.AccessCode;
import org.florense.domain.model.Anuncio;

import java.util.List;

public interface AccessCodePort {

    AccessCode createUpdate(AccessCode accessCode);

    AccessCode findById(Long id);
    List<AccessCode> listAll();

    void deleteById(Long id);
}
