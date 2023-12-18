package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.AccessCode;
import org.florense.inbound.adapter.dto.AccessCodeDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface AccessCodeDtoMapper {

    AccessCode toModel(AccessCodeDto dto);
    AccessCodeDto toDto(AccessCode model);
}
