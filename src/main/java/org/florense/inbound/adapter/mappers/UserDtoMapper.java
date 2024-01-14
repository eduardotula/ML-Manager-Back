package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.User;
import org.florense.inbound.adapter.dto.UserDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface UserDtoMapper {

    User toModel(UserDto dto);
    UserDto toDto(User model);
}
