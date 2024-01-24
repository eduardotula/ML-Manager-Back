package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.florense.outbound.adapter.postgre.mappers.AnuncioEntityMapper;
import org.florense.outbound.adapter.postgre.repository.AnuncioRepository;
import org.florense.outbound.port.postgre.AnuncioEntityPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AnuncioAdapter implements AnuncioEntityPort {

    @Inject
    AnuncioRepository repository;
    @Inject
    AnuncioEntityMapper mapper;

    @Override
    public Anuncio createUpdate(Anuncio anuncio){
        if(anuncio.getId() == null || anuncio.getCreatedAt() == null) anuncio.setCreatedAt(LocalDateTime.now());
        return mapper.toModel(repository.save(mapper.toEntity(anuncio)));
    }

    @Override
    public Anuncio findById(Long id){
        var anuncio = repository.find(id).orElse(null);
        return anuncio != null ? mapper.toModel(anuncio) : null;
    }

    @Override
    public Anuncio findAnyById(Long id) {
       var anuncio = repository.findAnyById(id).orElse(null);
       return anuncio != null ? mapper.toModel(anuncio) : null;
    }

    @Override
    public Anuncio findByMlId(String mlId, User user){
        List<AnuncioEntity> prd = repository.findByMlIdAndUserId(mlId, user.getId());
        return mapper.toModel(!prd.isEmpty() ? prd.get(0) : null);
    }

    @Override
    public Anuncio findAnyByMlId(String mlId, User user){
        var anuncio = repository.findAnyByMlId(mlId, user.getId()).orElse(null);
        return anuncio != null ? mapper.toModel(anuncio) : null;
    }

    @Override
    public List<Anuncio> listAll(User user){
        return repository.findByOrderByIdAsc(user.getId()).stream().map(mapper::toModel).collect(Collectors.toList());
    }
    @Override
    public List<Anuncio> listAllRegistered(User user){
        return repository.findRegisteredByOrderByIdAsc(user.getId()).stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void disableById(Long id){
        var anuncio = repository.findAnyById(id).orElse(null);
        if(anuncio != null){
            anuncio.setComplete(false);
            repository.save(anuncio);
        }
    }

    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }



}
