package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreProductsCatalogClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MercadoLivreProductsCatalogAdapter extends MercadoLivreAdapter{

    @RestClient
    @Inject
    MercadoLivreProductsCatalogClient mercadoLivreProductsCatalogClient;

    @Inject
    Logger logger;


    public void equalValueCatalog(){

    }
}
