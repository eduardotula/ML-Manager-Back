package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Reclamacao;
import org.florense.outbound.adapter.postgre.mappers.ReclamacaoEntityMapper;
import org.florense.outbound.adapter.postgre.repository.ReclamacaoRepository;
import org.florense.outbound.port.postgre.ReclamacaoEntityPort;

import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class ReclamacaoAdapter implements ReclamacaoEntityPort {

    @Inject
    ReclamacaoRepository repository;
    @Inject
    ReclamacaoEntityMapper mapper;

    @Override
    public Reclamacao createUpdate(Reclamacao reclamacao){
        if(Objects.isNull(reclamacao.getCreatedAt())) reclamacao.setCreatedAt(LocalDateTime.now());
        return mapper.toModel(repository.save(mapper.toEntity(reclamacao)));
    }

    @Override
    public Reclamacao findByReclamacaoId(Long reclamacaoId) {
        var reclamacao = repository.findByReclamacaoId(reclamacaoId).orElse(null);
        return reclamacao != null ? mapper.toModel(reclamacao) : null;
    }
}
