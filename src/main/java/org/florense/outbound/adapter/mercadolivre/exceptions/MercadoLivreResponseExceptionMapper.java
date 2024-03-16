package org.florense.outbound.adapter.mercadolivre.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.florense.domain.util.ObjectMapperUtil;

@Provider
@Blocking
public class MercadoLivreResponseExceptionMapper implements ResponseExceptionMapper<MercadoLivreClientException> {
    @Inject
    ObjectMapperUtil objectMapperUtil;

    @Override
    public MercadoLivreClientException toThrowable(Response response) {
        String output = response.readEntity(String.class);
        MercadoLivreClientException mercadoLivreClientException = new MercadoLivreClientException();
        try {
            mercadoLivreClientException = objectMapperUtil.mapper.readValue(output, MercadoLivreClientException.class);
        } catch (JsonProcessingException e) {
            mercadoLivreClientException.setMessage("Falha ao processar resposta de erro");
        }
        mercadoLivreClientException.setCompleteError(output);

        if(mercadoLivreClientException.getMessage().equalsIgnoreCase("invalid_token")) mercadoLivreClientException.setRefreshToken(true);
        return mercadoLivreClientException;
    }
}
