package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Reclamacao;
import org.florense.outbound.adapter.postgre.mappers.ReclamacaoEntityMapper;
import org.florense.outbound.adapter.postgre.repository.ReclamacaoRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class ReclamacaoAdapter {

    @Inject
    ReclamacaoRepository repository;
    @Inject
    ReclamacaoEntityMapper mapper;
    public Reclamacao createUpdate(Reclamacao reclamacao){
        if(Objects.isNull(reclamacao.getCreatedAt())) reclamacao.setCreatedAt(LocalDateTime.now());
        return mapper.toModel(repository.save(mapper.toEntity(reclamacao)));
    }
}
