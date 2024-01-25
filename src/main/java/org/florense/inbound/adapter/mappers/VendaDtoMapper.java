package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Venda;
import org.florense.inbound.adapter.dto.VendaDto;
import org.mapstruct.*;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface VendaDtoMapper {

    VendaDto toDto(Venda venda);

    Venda toModel(VendaDto vendaDto);

    @BeforeMapping
    default void processPicture(Venda venda, @MappingTarget VendaDto vendaDto){
        var fotos = venda.getAnuncio().getPictures();
        if(!fotos.isEmpty()){
            vendaDto.getAnuncio().setFotoCapa(fotos.get(0).getUrl());
        }
    }
}
