package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreService;
import org.florense.outbound.adapter.mercadolivre.mapper.MercadoLivreProdutoProduto;
import org.florense.outbound.port.mercadolivre.MercadoLivrePort;

@ApplicationScoped
public class MercadoLivreAdapter implements MercadoLivrePort {

    @RestClient
    @Inject
    MercadoLivreService mercadoLivreService;

    @Inject
    MercadoLivreProdutoProduto mapper;

    @Override
    public Produto getProduto(String mlId) {
        return mapper.toProduto(mercadoLivreService.produto(mlId));
    }
}
