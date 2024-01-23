package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Venda;
import org.florense.inbound.adapter.dto.VendaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface VendaDtoMapper {

    VendaDto toDto(Venda venda);

    Venda toModel(VendaDto vendaDto);
}
