package org.florense.outbound.adapter.mercadolivre.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.florense.domain.util.ObjectMapperUtil;
import org.jboss.logging.Logger;

@Provider
@Blocking
public class MercadoLivreResponseExceptionMapper implements ResponseExceptionMapper<MercadoLivreClientException> {
    @Inject
    ObjectMapperUtil objectMapperUtil;
    @Inject
    Logger logger;

    @Override
    public MercadoLivreClientException toThrowable(Response response) {
        String output = response.readEntity(String.class);
        MercadoLivreClientException mercadoLivreClientException = new MercadoLivreClientException("", 400, "", "", "", false);

        try {
            mercadoLivreClientException = objectMapperUtil.mapper.readValue(output, MercadoLivreClientException.class);
        } catch (JsonProcessingException e) {
            logger.error("Falha ao processar resposta de erro", e);
            mercadoLivreClientException.setMessage("Falha ao processar resposta de erro");
            logger.infof("resposta completa: %s", output);
        }
        mercadoLivreClientException.setCompleteError(output);
        if(response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()){
            if(output.contains("token")) mercadoLivreClientException.setRefreshToken(true);
        }

        return mercadoLivreClientException;
    }
}
