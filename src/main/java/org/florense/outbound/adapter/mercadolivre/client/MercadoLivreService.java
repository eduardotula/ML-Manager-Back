package org.florense.outbound.adapter.mercadolivre.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreProduto;

import java.util.Map;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
@RegisterClientHeaders(MercadoLivreServiceHeader.class)
public interface MercadoLivreService {

    @GET
    @Path("items/{mlId}")
    MercadoLivreProduto produto(@PathParam("mlId") String mlId);

    @GET
    @Path("/sites/MLB/listing_prices")
    Map<String, Object> getListingPrices(@QueryParam("price") Double price, @QueryParam("listing_type_id") String listingTypeId, @QueryParam("category_id") String categoryId);

    @GET
    @Path("/items/{mlId}/shipping_options")
    Map<String, Object> getFretePrice(@PathParam("mlId") String mlId, @QueryParam("zip_code") String zipCode);

    @GET
    @Path("/users/{userId}/items/search")
    Map<String, Object> listMlIds(@PathParam("userId") String userId, @QueryParam("status") String status, @QueryParam("offset") int offset, @QueryParam("limit") int limit);

}
