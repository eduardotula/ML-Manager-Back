package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.NotBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreResponseExceptionMapper;
import org.florense.outbound.adapter.mercadolivre.response.MLResponseWrapper;
import org.florense.outbound.adapter.mercadolivre.response.ProductCatalogResponse;

@ApplicationScoped
@RegisterProvider(MercadoLivreResponseExceptionMapper.class)
@Path("/products")
@RegisterRestClient(configKey = "ml-api")
public interface MercadoLivreProductsCatalogClient {

    @GET
    @Path("{/productId}/items")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    MLResponseWrapper<ProductCatalogResponse> vendasOrderDesc(
                                      @PathParam("productId") String productId,
                                      @QueryParam("offset") int offset,
                                      @QueryParam("sort") String sort,
                                      @QueryParam("limit") int limit,
                                      @NotBody String token) throws MercadoLivreClientException;

}
