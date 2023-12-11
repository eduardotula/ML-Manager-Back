package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreService;
import org.florense.outbound.port.mercadolivre.MercadoLivrePort;

@ApplicationScoped
public class MercadoLivreAdapter implements MercadoLivrePort {

    @Inject
    MercadoLivreService mercadoLivreService;
}
