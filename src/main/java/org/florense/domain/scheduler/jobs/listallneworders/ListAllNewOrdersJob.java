package org.florense.domain.scheduler.jobs.listallneworders;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.*;
import org.florense.domain.util.OrderScheduelerJobKeyGenerator;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.ZoneId;

public class ListAllNewOrdersJob implements Job {

    @Inject
    ListAllNewOrders listAllNewOrders;
    @Inject
    SchedulerJobEntityPort schedulerJobEntityPort;
    @Inject
    OrderScheduelerJobKeyGenerator nameGenerator;
    @Inject
    Logger logger;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            User user = (User) jobExecutionContext.getJobDetail().getJobDataMap().get("User");
            updateNextRunTime(jobExecutionContext, user);
            listAllNewOrders.execute(user);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
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
