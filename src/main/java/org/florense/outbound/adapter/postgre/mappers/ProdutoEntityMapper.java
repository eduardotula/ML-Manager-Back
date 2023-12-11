package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.postgre.entity.ProdutoEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public interface ProdutoEntityMapper {

    Produto toModel(ProdutoEntity produto);
    ProdutoEntity toEntity(Produto produto);
}
