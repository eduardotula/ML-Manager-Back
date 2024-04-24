package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.postgre.mappers.AccessCodeEntityMapper;
import org.florense.outbound.adapter.postgre.repository.UserRepository;
import org.florense.outbound.port.postgre.UserEntityPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserAdapter implements UserEntityPort {

    @Inject
    UserRepository repository;
    @Inject
    AccessCodeEntityMapper mapper;

    @Override
    public User createUpdate(User user){
        if(user.getId() == null) user.setCreatedAt(LocalDateTime.now());

        return mapper.toModel(repository.save(mapper.toEntity(user)));
    }

    @Override
    public User findById(Long id){
        var user = repository.findById(id).orElse(null);
        return user != null ? mapper.toModel(user) : null;
    }

    @Override
    public User findByMlIdUser(String mlUserId){
        var user = repository.findByUserIdML(mlUserId).orElse(null);
        return user != null ? mapper.toModel(user) : null;
    }

    @Override
    public List<User> listAll() {
        return repository.findAll().stream().map(mapper::toModel).collect(Collectors.toList());
    }
    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }

}
