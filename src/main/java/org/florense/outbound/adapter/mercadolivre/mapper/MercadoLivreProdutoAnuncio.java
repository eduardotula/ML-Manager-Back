package org.florense.outbound.adapter.mercadolivre.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreAnuncio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface MercadoLivreProdutoAnuncio {

    @Mapping(source = "title", target = "descricao")
    @Mapping(source = "permalink", target = "url")
    @Mapping(source = "category_id", target = "categoria")
    @Mapping(source = "price", target = "precoDesconto")
    Anuncio toAnuncio(MercadoLivreAnuncio ml);
}
