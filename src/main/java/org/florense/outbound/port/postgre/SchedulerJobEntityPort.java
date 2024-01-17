package org.florense.outbound.port.postgre;

import org.florense.domain.model.ScheduleJob;

import java.util.List;

public interface SchedulerJobEntityPort {
    ScheduleJob createUpdate(ScheduleJob scheduleJob);

    ScheduleJob findById(long id);

    ScheduleJob findByJobName(String jobName);

    List<ScheduleJob> listAll();

    void deleteAll();

    void deleteById(long id);
}
