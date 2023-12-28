package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.inbound.adapter.dto.AnuncioDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface AnuncioDtoMapper {

    AnuncioDto toDto(Anuncio anuncio);

    Anuncio toModel(AnuncioDto anuncioDto);
}
