package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.domain.util.ObjectMapperUtil;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.response.MLRefreshTokenResponse;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreProduto;

import java.util.Map;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
@RegisterClientHeaders(MercadoLivreServiceHeader.class)
public interface MercadoLivreService {

    @GET
    @Path("items/{mlId}")
    MercadoLivreProduto produto(@PathParam("mlId") String mlId) throws RuntimeException;

    @GET
    @Path("/sites/MLB/listing_prices")
    Map<String, Object> getListingPrices(@QueryParam("price") Double price, @QueryParam("listing_type_id") String listingTypeId, @QueryParam("category_id") String categoryId) throws RuntimeException;

    @GET
    @Path("/items/{mlId}/shipping_options")
    Map<String, Object> getFretePrice(@PathParam("mlId") String mlId, @QueryParam("zip_code") String zipCode) throws RuntimeException;

    @GET
    @Path("/users/{userId}/items/search")
    Map<String, Object> listMlIds(@PathParam("userId") String userId, @QueryParam("status") String status, @QueryParam("offset") int offset, @QueryParam("limit") int limit) throws RuntimeException;


    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        var body = response.readEntity(String.class);
        if (response.getStatus() == 401) {
            if (body.contains("invalid_token"))
                return new RuntimeException("status code: 401 generate new Key",new UnauthorizedAcessKeyException(""));
        }
        return new RuntimeException(String.format("status code: %s body: %s", response.getStatus(), body));
    }
}
