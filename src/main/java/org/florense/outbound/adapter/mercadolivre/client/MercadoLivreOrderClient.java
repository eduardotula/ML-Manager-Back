package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.NotBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreResponseExceptionMapper;
import org.florense.outbound.adapter.mercadolivre.response.MLOrderWrapperResponse;

@ApplicationScoped
@RegisterProvider(MercadoLivreResponseExceptionMapper.class)
@Path("/orders")
@RegisterRestClient(configKey = "ml-api")
public interface MercadoLivreOrderClient {

    @GET
    @Path("/search")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    MLOrderWrapperResponse  vendasOrderDesc(@QueryParam("seller") String userId,
                                            @QueryParam("order.date_created.from")String dataInicial,
                                            @QueryParam("order.date_created.to") String dataFinal,
                                            @QueryParam("order.status") String status,
                                            @QueryParam("offset") int offset,
                                            @QueryParam("sort") String sort,
                                            @QueryParam("limit") int limit,
                                            @NotBody String token) throws MercadoLivreClientException;

    @GET
    @Path("/search")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    MLOrderWrapperResponse vendasOrderDescByStatus(@QueryParam("seller") String userId,
                                                   @QueryParam("order.status") String status,
                                                   @QueryParam("sort") String sort,
                                                   @QueryParam("offset") int offset,
                                                   @QueryParam("limit") int limit,
                                                   @NotBody String token) throws MercadoLivreClientException;

}
