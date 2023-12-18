package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.AccessCode;
import org.florense.outbound.adapter.postgre.mappers.AccessCodeEntityMapper;
import org.florense.outbound.adapter.postgre.repository.AccessCodeRepository;
import org.florense.outbound.port.postgre.AccessCodePort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AccessCodeAdapter implements AccessCodePort {

    @Inject
    AccessCodeRepository repository;
    @Inject
    AccessCodeEntityMapper mapper;

    @Override
    public AccessCode createUpdate(AccessCode accessCode){
        if(accessCode.getId() == null) accessCode.setCreatedAt(LocalDateTime.now());

        return mapper.toModel(repository.save(mapper.toEntity(accessCode)));
    }

    @Override
    public AccessCode findById(Long id){
        return mapper.toModel(repository.findById(id).orElseGet(null));
    }

    @Override
    public List<AccessCode> listAll() {
        return repository.findAll().stream().map(mapper::toModel).collect(Collectors.toList());
    }
    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }

}
