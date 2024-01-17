package org.florense.outbound.adapter.postgre.entity;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "scheduleJob")
@Table(name = "schedule_job")
public class ScheduleJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "job_name", columnDefinition = "VARCHAR(300)")
    private String jobName;

    @NotNull
    @Column(name = "job_group_name", columnDefinition = "VARCHAR(300)")
    private String jobGroupName;

    @NotNull
    @Column(name = "next_run_time")
    private LocalDateTime nextRunTime;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
