package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.AccessCode;
import org.florense.outbound.adapter.postgre.entity.AccessCodeEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface AccessCodeEntityMapper {

    AccessCode toModel(AccessCodeEntity entity);
    AccessCodeEntity toEntity(AccessCode model);
}
