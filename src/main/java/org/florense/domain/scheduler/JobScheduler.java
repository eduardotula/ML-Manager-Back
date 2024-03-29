package org.florense.domain.scheduler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class JobScheduler {

    @Inject
    Scheduler quartz;

    public ScheduleJob createJob(JobKey jobKey, String cron, Class<? extends Job> job, Object... jobParameters) throws SchedulerException {
        var activateJob = JobBuilder.newJob(job).withIdentity(jobKey).build();

        for (Object jobParameter : jobParameters)
            activateJob.getJobDataMap().put(jobParameter.getClass().getSimpleName(), jobParameter);

        var activationTrigger = TriggerBuilder.newTrigger().withSchedule(
                CronScheduleBuilder.cronSchedule(cron)).build();

        Date runTime = quartz.scheduleJob(activateJob, activationTrigger);
        LocalDateTime localDateTimeRunTime = runTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return ScheduleJob.builder().jobName(jobKey.getName()).jobGroupName(jobKey.getGroup())
                .nextRunTime(localDateTimeRunTime).build();
    }

    public List<String> getScheduledJobs() {
        List<String> jobsList = new ArrayList<>();

        try {
            for (String groupName : quartz.getJobGroupNames()) {

                for (JobKey jobKey : quartz.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    String jobName = jobKey.getName();

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) quartz.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();

                    jobsList.add("[jobName] : " + jobName + " - " + nextFireTime);

                }
            }
        } catch (SchedulerException e) {
            throw new IllegalStateException("Falha ao obter agendamentos");
        }
        return jobsList;
    }

    public void deleteJob(String jobName) {
        try {
            quartz.deleteJob(new JobKey(jobName));
        } catch (SchedulerException e) {
            throw new IllegalStateException(String.format("Falha ao deletar agendamento %s"));
        }
    }


}
