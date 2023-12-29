package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Venda;
import org.florense.outbound.adapter.postgre.entity.VendaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface VendaEntityMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "anuncio.id", target = "anuncioId")
    Venda toModel(VendaEntity vendaEntity);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "anuncioId", target = "anuncio.id")
    VendaEntity toEntity(Venda venda);
}
