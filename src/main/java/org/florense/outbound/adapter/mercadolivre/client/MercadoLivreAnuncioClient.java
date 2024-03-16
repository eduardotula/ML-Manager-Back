package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.NotBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreResponseExceptionMapper;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreAnuncioResponse;

import java.util.Map;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
@RegisterProvider(MercadoLivreResponseExceptionMapper.class)
public interface MercadoLivreAnuncioClient {

    @GET
    @Path("items/{mlId}")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    MercadoLivreAnuncioResponse anuncio(@PathParam("mlId") String mlId, @NotBody String token) throws MercadoLivreClientException;

    @GET
    @Path("/sites/MLB/listing_prices")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> getListingPrices(@QueryParam("price") double price,
                                         @QueryParam("listing_type_id") String listingTypeId,
                                         @QueryParam("category_id") String categoryId,
                                         @NotBody String token) throws MercadoLivreClientException;

    @GET
    @Path("/items/{mlId}/shipping_options")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> getFretePrice(@PathParam("mlId") String mlId, @QueryParam("zip_code") String zipCode,
                                      @NotBody String token) throws MercadoLivreClientException;

    @GET
    @Path("/shipments/{shippingId}/costs")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> getFretePriceByShippingId(@PathParam("shippingId") String shippingId, @NotBody String token) throws MercadoLivreClientException;
    @GET
    @Path("/users/{userId}/items/search")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    Map<String, Object> listMlIds(@PathParam("userId") String userId, @QueryParam("status") String status,
                                  @QueryParam("offset") int offset,
                                  @NotBody String token) throws MercadoLivreClientException;


//    @ClientExceptionMapper
//    static RuntimeException toException(Response response) {
//        var body = response.readEntity(String.class);
//        if (response.getStatus() == 401) {
//            if (body.contains("invalid_token"))
//                return new RuntimeException("status code: 401 generate new Key",new UnauthorizedAcessKeyException(""));
//        }
//        return new RuntimeException(String.format("status code: %s body: %s", response.getStatus(), body));
//    }
}
