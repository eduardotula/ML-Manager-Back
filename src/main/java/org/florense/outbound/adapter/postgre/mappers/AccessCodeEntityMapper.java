package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.postgre.entity.UserEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface AccessCodeEntityMapper {

    User toModel(UserEntity entity);
    UserEntity toEntity(User model);
}
