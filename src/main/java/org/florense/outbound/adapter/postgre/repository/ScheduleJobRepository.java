package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long> {

    ScheduleJobEntity findByJobName(String jobName);
}
