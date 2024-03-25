package org.florense.domain.scheduler;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.florense.domain.model.ScheduleJob;
import org.florense.domain.model.User;
import org.florense.domain.scheduler.jobs.checkorderstatuschange.CheckOrderStatusChangeJob;
import org.florense.domain.util.CronUtils;
import org.florense.domain.util.OrderScheduelerJobKeyGenerator;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.florense.domain.scheduler.jobs.listallneworders.ListAllNewOrdersJob;

import java.util.List;

@ApplicationScoped
public class OrderScheduler {

    @Inject
    SchedulerJobEntityPort schedulerJobEntityPort;
    @ConfigProperty(name = "scheduler.job.order.refresh_delay_minutes")
    int orderRefreshDelay;
    @Inject
    OrderScheduelerJobKeyGenerator jobKeyGenerator;
    @ConfigProperty(name = "scheduler.job.order.search_order_limit_months")
    int orderRefreshDelayStausChangeHours;
    @Inject
    JobScheduler jobScheduler;
    @Inject
    UserEntityPort userEntityPort;
    @Inject
    CronUtils cronUtils;

    public void createScheduleOrdersJobByUser(List<User> users){
        try {
            for (User user: users){
                JobKey jobKey = jobKeyGenerator.createSearchOrderKey(user);
                String cron = cronUtils.toCronTimeRepeatEveryMinute(orderRefreshDelay);
                //String cron = "0 */2 * ? * *";

                ScheduleJob scheduleJob = jobScheduler.createJob(jobKey, cron, ListAllNewOrdersJob.class, user);
                schedulerJobEntityPort.createUpdate(scheduleJob);
            }

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSchedulerCheckOrderStatus(List<User> users){
        try{
            for(User user: users){
                JobKey jobKey = jobKeyGenerator.createStatusChangeOrder(user);
                String cron = cronUtils.toCronTimeRepeatEveryHour(orderRefreshDelayStausChangeHours);
                //String cron = "0 */2 * ? * *";
                ScheduleJob scheduleJob = jobScheduler.createJob(jobKey, cron, CheckOrderStatusChangeJob.class, user);
                schedulerJobEntityPort.createUpdate(scheduleJob);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void createJobsOnStartUp(@Observes StartupEvent event){
        schedulerJobEntityPort.deleteAll();
        List<User> users = userEntityPort.listAll();
        createScheduleOrdersJobByUser(users);
        createSchedulerCheckOrderStatus(users);
    }
}
