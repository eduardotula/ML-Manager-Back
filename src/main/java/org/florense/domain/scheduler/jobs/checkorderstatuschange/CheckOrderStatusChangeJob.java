package org.florense.domain.scheduler.jobs.checkorderstatuschange;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.ScheduleJob;
import org.florense.domain.model.User;
import org.florense.domain.util.OrderScheduelerJobKeyGenerator;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.ZoneId;

@ApplicationScoped
public class CheckOrderStatusChangeJob implements Job {

    @Inject
    CheckOrderStatusChange checkOrderStatusChange;
    @Inject
    SchedulerJobEntityPort schedulerJobEntityPort;
    @Inject
    OrderScheduelerJobKeyGenerator nameGenerator;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            User user = (User) jobExecutionContext.getJobDetail().getJobDataMap().get("User");
            updateNextRunTime(jobExecutionContext, user);
            checkOrderStatusChange.execute(user);
        }catch (Exception e){
            throw new JobExecutionException(e);
        }
    }

    private void updateNextRunTime(JobExecutionContext jobExecutionContext, User user) {
        ScheduleJob scheduleJob = schedulerJobEntityPort.findByJobName(nameGenerator.createSearchOrderKey(user).getName());
        if (scheduleJob != null) {
            scheduleJob.setNextRunTime(jobExecutionContext.getTrigger().getNextFireTime().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime());
            schedulerJobEntityPort.createUpdate(scheduleJob);
        }
    }
}
