package org.florense.outbound.adapter.mercadolivre.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreProduto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface MercadoLivreProdutoProduto {

    @Mapping(source = "title", target = "descricao")
    @Mapping(source = "permalink", target = "url")
    @Mapping(source = "category_id", target = "categoria")
    @Mapping(source = "price", target = "precoDesconto")
    Produto toProduto(MercadoLivreProduto ml);
}
