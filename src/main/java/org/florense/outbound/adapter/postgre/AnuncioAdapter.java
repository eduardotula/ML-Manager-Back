package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Anuncio;
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
        if(anuncio.getId() == null) anuncio.setCreatedAt(LocalDateTime.now());
        return mapper.toModel(repository.save(mapper.toEntity(anuncio)));
    }

    @Override
    public Anuncio findById(Long id){
        var anuncio = repository.findById(id).orElse(null);
        return anuncio != null ? mapper.toModel(anuncio) : null;
    }

    @Override
    public Anuncio findByMlId(String mlId){
        List<AnuncioEntity> prd = repository.findByMlId(mlId);
        return mapper.toModel(!prd.isEmpty() ? prd.get(0) : null);
    }

    @Override
    public List<Anuncio> listAll(){
        return repository.findByOrderByIdAsc().stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }

}
