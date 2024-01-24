package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Url;
import org.florense.outbound.adapter.postgre.entity.UrlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface UrlEntityMapper {

    Url toModel(UrlEntity urlEntity);
    UrlEntity toEntity(Url url);


}
