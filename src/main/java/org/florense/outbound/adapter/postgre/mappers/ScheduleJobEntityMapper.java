package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.ScheduleJob;
import org.florense.outbound.adapter.postgre.entity.ScheduleJobEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface ScheduleJobEntityMapper {

    ScheduleJobEntity toEntity(ScheduleJob job);

    ScheduleJob toModel(ScheduleJobEntity scheduleJobEntity);
}
