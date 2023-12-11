package org.florense.outbound.adapter.mercadolivre.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreProduto;

@ApplicationScoped
@Path("/")
@RegisterRestClient(configKey = "ml-api")
@RegisterClientHeaders(MercadoLivreServiceHeader.class)
public interface MercadoLivreService {

    @GET
    @Path("items/{mlId}")
    MercadoLivreProduto produto(@PathParam("mlId") String mlId);

}
