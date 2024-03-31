package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.util.Log;
import org.florense.outbound.adapter.postgre.mappers.LogEntityEntityMapper;
import org.florense.outbound.adapter.postgre.repository.LogRepository;

@ApplicationScoped
public class LogAdapter {

    @Inject
    LogRepository repository;
    @Inject
    LogEntityEntityMapper mapper;

    public Log createUpdate(Log log){
        log.setErrorMessage(log.getErrorMessage().substring(0, 500));
        return mapper.toModel(repository.save(mapper.toEntity(log)));
    }
}
