package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.ScheduleJob;
import org.florense.inbound.adapter.dto.ScheduleJobDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface ScheduleJobDtoMapper {

    ScheduleJobDto toDto(ScheduleJob scheduleJob);

    ScheduleJob toModel(ScheduleJobDto scheduleJobDto);
}
