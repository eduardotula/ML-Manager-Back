package org.florense.domain.model;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleJob {

    private Integer id;

    private String jobName;

    private String jobGroupName;

    private LocalDateTime nextRunTime;
    private LocalDateTime createdAt;
}
