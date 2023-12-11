package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.postgre.mappers.ProdutoEntityMapper;
import org.florense.outbound.adapter.postgre.repository.ProdutoRepository;
import org.florense.outbound.port.postgre.ProdutoEntityPort;

@ApplicationScoped
public class ProdutoAdapter implements ProdutoEntityPort {

    @Inject
    ProdutoRepository repository;
    @Inject
    ProdutoEntityMapper mapper;

    @Override
    public Produto saveUpdate(Produto produto){
        return mapper.toModel(repository.save(mapper.toEntity(produto)));
    }

}
