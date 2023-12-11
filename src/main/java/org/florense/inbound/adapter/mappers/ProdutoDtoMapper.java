package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Produto;
import org.florense.inbound.adapter.dto.ProdutoDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface ProdutoDtoMapper {

    ProdutoDto toDto(Produto produto);

    Produto toModel(ProdutoDto produtoDto);
}
