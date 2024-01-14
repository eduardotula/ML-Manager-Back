package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.quarkus.rest.client.reactive.NotBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderWrapperResponse;

@ApplicationScoped
@Path("/orders")
@RegisterRestClient(configKey = "ml-api")
public interface MercadoLivreOrderService {

    @GET
    @Path("/search/sort=date_desc")
    @ClientHeaderParam(name = "header_auth", value = "Bearer{token}")
    MLOrderWrapperResponse  vendasOrderDesc(@QueryParam("seller") String userId,
                                            @QueryParam("offset") int offset,
                                            @NotBody String bearer) throws RuntimeException;

    @GET
    @Path("/search/sort=date_desc")
    @ClientHeaderParam(name = "header_auth", value = "Bearer{token}")
    MLOrderWrapperResponse vendasOrderDescByStatus(@QueryParam("seller") String userId,
                                                   @QueryParam("order.status") String status,
                                                   @QueryParam("offset") int offset,
                                                   @NotBody String bearer) throws RuntimeException;

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
