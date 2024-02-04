package org.florense.domain.scheduler;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.florense.domain.model.ScheduleJob;
import org.florense.domain.model.User;
import org.florense.domain.util.CronUtils;
import org.florense.domain.util.OrderScheduelerJobKeyGenerator;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.florense.domain.scheduler.jobs.ListAllNewOrdersJob;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class OrderScheduler {

    @Inject
    SchedulerJobEntityPort schedulerJobEntityPort;
    @ConfigProperty(name = "scheduler.job.order.refresh_delay_minutes")
    int orderRefreshDelay;
    @Inject
    OrderScheduelerJobKeyGenerator jobKeyGenerator;
    @Inject
    JobScheduler jobScheduler;
    @Inject
    UserEntityPort userEntityPort;
    @Inject
    CronUtils cronUtils;

    public void createScheduleOrdersJobByUser(User user){
        JobKey jobKey = jobKeyGenerator.createJobKey(user);
        String cron = cronUtils.toCronTimeRepeatEveryMinute(orderRefreshDelay);
        //String cron = "0 */2 * ? * *";

        LocalDateTime localDateTimeRunTime = null;
        try {
            localDateTimeRunTime = jobScheduler.createJob(jobKey, cron, ListAllNewOrdersJob.class, user);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }


        ScheduleJob scheduleJob = schedulerJobEntityPort.findByJobName(jobKey.getName());
        if(scheduleJob == null){
            scheduleJob = ScheduleJob.builder().jobName(jobKey.getName()).jobGroupName(jobKey.getGroup())
                    .nextRunTime(localDateTimeRunTime).build();
        }
        else{
            scheduleJob.setNextRunTime(localDateTimeRunTime);
        }

        schedulerJobEntityPort.createUpdate(scheduleJob);
    }

    public void createJobsOnStartUp(@Observes StartupEvent event){
        List<User> users = userEntityPort.listAll();
        for (User user : users) {
            createScheduleOrdersJobByUser(user);
        }
    }
}
