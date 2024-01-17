package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.ScheduleJob;
import org.florense.outbound.adapter.postgre.entity.ScheduleJobEntity;
import org.florense.outbound.adapter.postgre.mappers.ScheduleJobEntityMapper;
import org.florense.outbound.adapter.postgre.repository.ScheduleJobRepository;
import org.florense.outbound.port.postgre.SchedulerJobEntityPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ScheduleJobAdapter implements SchedulerJobEntityPort {

    @Inject
    ScheduleJobRepository repository;
    @Inject
    ScheduleJobEntityMapper entityMapper;

    @Override
    public ScheduleJob createUpdate(ScheduleJob scheduleJob){
        ScheduleJobEntity scheduleJobEntity = entityMapper.toEntity(scheduleJob);

        if(scheduleJobEntity.getCreatedAt() == null) scheduleJobEntity.setCreatedAt(LocalDateTime.now());

        return entityMapper.toModel(repository.save(scheduleJobEntity));
    }

    @Override
    public ScheduleJob findById(long id){
        var scheduleJob = repository.findById(id).orElse(null);
        return scheduleJob != null ? entityMapper.toModel(scheduleJob) : null;
    }

    @Override
    public ScheduleJob findByJobName(String jobName){
        var scheduleJobEntity = repository.findByJobName(jobName);
        return scheduleJobEntity != null ? entityMapper.toModel(scheduleJobEntity) : null;
    }

    @Override
    public List<ScheduleJob> listAll(){
        return repository.findAll().stream().map(entityMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void deleteAll(){
        repository.deleteAll();
    }

    @Override
    public void deleteById(long id){
        repository.deleteById(id);
    }
}
