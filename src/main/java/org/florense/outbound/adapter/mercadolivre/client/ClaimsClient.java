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
import org.florense.outbound.adapter.mercadolivre.response.MLClaimWrapper;

import java.util.Map;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
@RegisterProvider(MercadoLivreResponseExceptionMapper.class)
public interface ClaimsClient {

    @GET
    @Path("v1/claims/search")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    MLClaimWrapper claim(@QueryParam("id") String claimId,
                         @NotBody String token) throws MercadoLivreClientException;
}
