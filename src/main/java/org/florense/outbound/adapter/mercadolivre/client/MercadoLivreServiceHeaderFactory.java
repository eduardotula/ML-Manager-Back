package org.florense.outbound.adapter.mercadolivre.client;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.florense.domain.usecase.UserUseCase;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

public class MercadoLivreServiceHeaderFactory implements ClientHeadersFactory {

    @Inject
    UserUseCase useCase;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {

        var result = new MultivaluedHashMap<String, String>();
        result.add("Accept", "application/json");
        result.add("Cache-Control", "no-cache");
        result.add("Content-Type", "application/json");

        try {
            var auth = useCase.get();
            if(auth == null) throw new IllegalArgumentException("");
            result.add("Authorization", "Bearer " + auth.getAccessCode());
        } catch (FailRequestRefreshTokenException e) {}
        return result;
    }
}
