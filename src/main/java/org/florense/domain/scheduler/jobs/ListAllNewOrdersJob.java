package org.florense.domain.scheduler.jobs;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.*;
import org.florense.domain.util.OrderScheduelerJobKeyGenerator;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.port.mercadolivre.MercadoLivreAnuncioPort;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.ZoneId;
import java.util.*;

public class ListAllNewOrdersJob implements Job {

    @Inject
    ListAllNewOrders listAllNewOrders;
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
            listAllNewOrders.execute(user);
        }catch (Exception e){
            throw new JobExecutionException(e);
        }

    }

    private void updateNextRunTime(JobExecutionContext jobExecutionContext, User user) {
        ScheduleJob scheduleJob = schedulerJobEntityPort.findByJobName(nameGenerator.createJobKey(user).getName());
        if (scheduleJob != null) {
            scheduleJob.setNextRunTime(jobExecutionContext.getTrigger().getNextFireTime().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDateTime());
            schedulerJobEntityPort.createUpdate(scheduleJob);
        }
    }
}
