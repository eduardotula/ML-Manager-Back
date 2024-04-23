package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Reclamacao;
import org.florense.outbound.adapter.postgre.entity.ReclamacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface ReclamacaoEntityMapper {

    @Mapping(source = "orderId", target = "order.id")
    ReclamacaoEntity toEntity(Reclamacao reclamacao);

    @Mapping(source = "order.id", target = "orderId")
    Reclamacao toModel(ReclamacaoEntity reclamacao);

}
