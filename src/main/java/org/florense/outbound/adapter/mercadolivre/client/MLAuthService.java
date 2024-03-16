package org.florense.outbound.adapter.mercadolivre.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.response.MLRefreshTokenResponse;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
public interface MLAuthService {


    @POST
    @Path("/oauth/token")
    MLRefreshTokenResponse refreshToken(@QueryParam("grant_type") String grantType,
                                        @QueryParam("client_id") String appId,
                                        @QueryParam("client_secret") String clientSecret,
                                        @QueryParam("refresh_token") String refreshToken);

}
