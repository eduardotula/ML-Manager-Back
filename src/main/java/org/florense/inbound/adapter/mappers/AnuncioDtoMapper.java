package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Url;
import org.florense.inbound.adapter.dto.AnuncioDto;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = UrlDtoMapper.class)
public interface AnuncioDtoMapper {

    AnuncioDto toDto(Anuncio anuncio);

    Anuncio toModel(AnuncioDto anuncioDto);
}
