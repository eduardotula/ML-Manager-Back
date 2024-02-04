package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Url;
import org.florense.inbound.adapter.dto.UrlDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface UrlDtoMapper {

    UrlDto toDto(Url url);


    Url toModel(UrlDto urlDto);


}
