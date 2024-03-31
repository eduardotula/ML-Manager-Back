package org.florense.inbound.adapter.exceptions.handlers;


import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Inject
    Logger log;
    @Override
    public Response toResponse(Exception e) {
        log.error(e.getMessage(),e);
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(String.format("Erro: %s",e.getMessage())).build();
    }
}
