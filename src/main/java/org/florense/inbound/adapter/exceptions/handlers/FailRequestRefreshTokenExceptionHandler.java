package org.florense.inbound.adapter.exceptions.handlers;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.florense.inbound.adapter.exceptions.ErrorResponse;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

@Provider
public class FailRequestRefreshTokenExceptionHandler implements ExceptionMapper<FailRequestRefreshTokenException> {
    @Override
    public Response toResponse(FailRequestRefreshTokenException e) {
        return Response
                .status(HttpResponseStatus.BAD_REQUEST.code())
                .entity(ErrorResponse.builder()
                        .message(e.getMessage())
                        .build()
                )
                .build();
    }
}
