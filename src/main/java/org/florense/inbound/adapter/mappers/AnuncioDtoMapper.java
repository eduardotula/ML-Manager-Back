package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.inbound.adapter.dto.anuncios.AnuncioDto;
import org.mapstruct.*;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = UrlDtoMapper.class)
public interface AnuncioDtoMapper {

    AnuncioDto toDto(Anuncio anuncio);

    Anuncio toModel(AnuncioDto anuncioDto);
}
