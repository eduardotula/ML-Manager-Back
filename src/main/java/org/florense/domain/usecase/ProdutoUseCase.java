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

    public Produto createUpdate(Produto produto){
        return produtoEntityPort.saveUpdate(produto);
    }

    //Cria e atualiza com mercado livre
    public Produto createMlSearch(Produto produto){
        var existProd = produtoEntityPort.findByMlId(produto.getMlId());
        if(existProd != null) throw new IllegalArgumentException(String.format("Produto com id: %s já cadastrado",produto.getMlId()));

        Produto completeProduto = mercadoLivreAdapter.getProduto(produto.getMlId());
        completeProduto.setCusto(produto.getCusto());
        completeProduto.setCsosn(produto.getCsosn());

        return produtoEntityPort.saveUpdate(completeProduto);
    }

    //Somente atualiza um produto
    public Produto updateSimple(Produto produto){
        var existProd = produtoEntityPort.findByMlId(produto.getMlId());
        if(Objects.isNull(existProd)) throw new IllegalArgumentException(String.format("Produto com id %s não encontrado", produto.getMlId()));
        produto.setCsosn(produto.getCsosn());
        produto.setCusto(produto.getCusto());

        return produtoEntityPort.saveUpdate(existProd);
    }

    //Atualiza dados somente do mercado livre
    public Produto updateSearch(String mlId){
        var existProd = produtoEntityPort.findByMlId(mlId);
        if(Objects.isNull(existProd)){
            throw new IllegalArgumentException(String.format("Produto com mlId: %s não encontrado", mlId ));
        }

        Produto completeProduto = mercadoLivreAdapter.getProduto(mlId);
        completeProduto.update(existProd);
        return produtoEntityPort.saveUpdate(completeProduto);
    }

    public List<Produto> listAll(){
        return produtoEntityPort.listAll();
    }

    public void deleteBy(Long id){
        if(Objects.isNull(produtoEntityPort.findById(id)))
            throw new IllegalArgumentException(String.format("Produto com id %s não encontrado", id));

        try{
            produtoEntityPort.deleteById(id);
        }catch (Exception e){
            throw new IllegalStateException(String.format("Falha ao apagar produto com id: %s",id));
        }
    }
}
