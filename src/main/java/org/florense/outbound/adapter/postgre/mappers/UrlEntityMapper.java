package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Url;
import org.florense.outbound.adapter.postgre.entity.UrlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface UrlEntityMapper {

    @Mapping(source = "anuncio.id", target = "anuncioId")
    Url toModel(UrlEntity urlEntity);
    @Mapping(source = "anuncioId", target = "anuncio.id")
    UrlEntity toEntity(Url url);


}
