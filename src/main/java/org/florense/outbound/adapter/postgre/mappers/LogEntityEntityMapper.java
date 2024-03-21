package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.util.Log;
import org.florense.outbound.adapter.postgre.entity.LogEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = {VendaEntityMapper.class})
public interface LogEntityEntityMapper {

    Log toModel(LogEntity logEntity);
    LogEntity toEntity(Log log);
}
