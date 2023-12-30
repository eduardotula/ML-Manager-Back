package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderResponse;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderWrapperResponse;

import java.util.List;

@ApplicationScoped
@Path("/orders")
@RegisterRestClient(configKey = "ml-api")
@RegisterClientHeaders(MercadoLivreServiceHeader.class)
public interface MercadoLivreOrderService {

    @GET
    @Path("/search/sort=date_desc")
    MLOrderWrapperResponse  vendasOrderDesc(@QueryParam("seller") String userId, @QueryParam("offset") int offset) throws RuntimeException;

    @GET
    @Path("/search/sort=date_desc")
    MLOrderWrapperResponse vendasOrderDescByStatus(@QueryParam("seller") String userId, @QueryParam("order.status") String status, @QueryParam("offset") int offset) throws RuntimeException;

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        var body = response.readEntity(String.class);
        if (response.getStatus() == 401) {
            if (body.contains("token"))
                return new RuntimeException("status code: 401 generate new Key",new UnauthorizedAcessKeyException(""));
        }
        return new RuntimeException(String.format("status code: %s body: %s", response.getStatus(), body));
    }
}
