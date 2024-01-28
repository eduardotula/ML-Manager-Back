package org.florense.inbound.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleJobDto {

    private int id;

    private String jobName;

    private String jobGroupName;

    private LocalDateTime nextRunTime;
    private LocalDateTime createdAt;
}
