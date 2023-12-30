package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Venda;
import org.florense.inbound.adapter.dto.VendaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface VendaDtoMapper {

    @Mapping(source = "anuncio.mlId", target = "mlId")
    @Mapping(source = "anuncio.descricao", target = "descricao")
    VendaDto toDto(Venda venda);

    @Mapping(source = "mlId", target = "anuncio.mlId")
    @Mapping(source = "descricao", target = "anuncio.descricao")
    Venda toModel(VendaDto vendaDto);
}
