package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.postgre.entity.ProdutoEntity;
import org.florense.outbound.adapter.postgre.mappers.ProdutoEntityMapper;
import org.florense.outbound.adapter.postgre.repository.ProdutoRepository;
import org.florense.outbound.port.postgre.ProdutoEntityPort;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Produto findById(Long id){
        return mapper.toModel(repository.findById(id).orElse(null));
    }

    @Override
    public Produto findByMlId(String mlId){
        List<ProdutoEntity> prd = repository.findByMlId(mlId);
        return mapper.toModel(!prd.isEmpty() ? prd.get(0) : null);
    }

    @Override
    public List<Produto> listAll(){
        return repository.findAll().stream().map(mapper::toModel).collect(Collectors.toList());
    }

}
