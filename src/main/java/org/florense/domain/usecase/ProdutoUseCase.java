package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Produto;
import org.florense.outbound.port.mercadolivre.MercadoLivrePort;
import org.florense.outbound.port.postgre.ProdutoEntityPort;

import java.util.List;
import java.util.Objects;

@RequestScoped
public class ProdutoUseCase {

    @Inject
    ProdutoEntityPort produtoEntityPort;
    @Inject
    MercadoLivrePort mercadoLivreAdapter;

    public Produto saveUpdate(Produto produto){
        return produtoEntityPort.saveUpdate(produto);

    }

    public Produto saveUpdateByMl(Produto produto){
        var existProd = produtoEntityPort.findByMlId(produto.getMlId());
        Produto completeProduto = mercadoLivreAdapter.getProduto(produto.getMlId());
        if(Objects.nonNull(existProd)){
            completeProduto.setId(existProd.getId());
        }

        return produtoEntityPort.saveUpdate(completeProduto);
    }

    public Produto updateExistProd(String mlId){
        var existProd = produtoEntityPort.findByMlId(mlId);
        if(Objects.isNull(existProd)){
            throw new IllegalArgumentException(String.format("Produto com mlId: %s não encontrado", mlId ));
        }

        Produto completeProduto = mercadoLivreAdapter.getProduto(mlId);
        completeProduto.setId(existProd.getId());
        return produtoEntityPort.saveUpdate(completeProduto);
    }

    public List<Produto> listAll(){
        return produtoEntityPort.listAll();
    }
}
