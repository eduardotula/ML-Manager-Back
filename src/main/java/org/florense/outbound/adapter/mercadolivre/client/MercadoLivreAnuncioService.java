package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.quarkus.rest.client.reactive.NotBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreAnuncioResponse;

import java.util.Map;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
public interface MercadoLivreAnuncioService {

    @GET
    @Path("items/{mlId}")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    MercadoLivreAnuncioResponse anuncio(@PathParam("mlId") String mlId, @NotBody String bearer) throws RuntimeException;

    @GET
    @Path("/sites/MLB/listing_prices")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> getListingPrices(@QueryParam("price") Double price,
                                         @QueryParam("listing_type_id") String listingTypeId,
                                         @QueryParam("category_id") String categoryId,
                                         @NotBody String bearer) throws RuntimeException;

    @GET
    @Path("/items/{mlId}/shipping_options")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> getFretePrice(@PathParam("mlId") String mlId, @QueryParam("zip_code") String zipCode,
                                      @NotBody String bearer) throws RuntimeException;

    @GET
    @Path("/users/{userId}/items/search")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> listMlIds(@PathParam("userId") String userId, @QueryParam("status") String status,
                                  @QueryParam("offset") int offset,
                                  @NotBody String bearer) throws RuntimeException;


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
