package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Produto;
import org.florense.outbound.port.mercadolivre.MercadoLivrePort;
import org.florense.outbound.port.postgre.ProdutoEntityPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        completeProduto.setTaxaML(mercadoLivreAdapter.getTarifas(completeProduto.getPrecoDesconto(), completeProduto.getCategoria()));
        if(completeProduto.getPrecoDesconto() >= 80)
            completeProduto.setCustoFrete(mercadoLivreAdapter.getFrete(completeProduto.getMlId(),"06950000"));
        else completeProduto.setCustoFrete(0.0);

        completeProduto.setCusto(produto.getCusto());
        completeProduto.setCsosn(produto.getCsosn());
        completeProduto.setLucro(calculateLucro(completeProduto));

        return produtoEntityPort.saveUpdate(completeProduto);
    }

    //Somente atualiza um produto
    public Produto updateSimple(Produto produto){
        var existProd = produtoEntityPort.findByMlId(produto.getMlId());
        if(Objects.isNull(existProd)) throw new IllegalArgumentException(String.format("Produto com id %s não encontrado", produto.getMlId()));
        existProd.setCsosn(produto.getCsosn());
        existProd.setCusto(produto.getCusto());
        existProd.setLucro(calculateLucro(existProd));

        return produtoEntityPort.saveUpdate(existProd);
    }

    //Atualiza dados somente do mercado livre
    public Produto updateSearch(String mlId){
        var existProd = produtoEntityPort.findByMlId(mlId);
        if(Objects.isNull(existProd)){
            throw new IllegalArgumentException(String.format("Produto com mlId: %s não encontrado", mlId ));
        }

        Produto completeProduto = mercadoLivreAdapter.getProduto(mlId);
        completeProduto.setTaxaML(mercadoLivreAdapter.getTarifas(completeProduto.getPrecoDesconto(), completeProduto.getCategoria()));
        if(completeProduto.getPrecoDesconto() >= 80)
            completeProduto.setCustoFrete(mercadoLivreAdapter.getFrete(completeProduto.getMlId(),"06950000"));
        else completeProduto.setCustoFrete(0.0);
        completeProduto.update(existProd);
        completeProduto.setCsosn(existProd.getCsosn());
        completeProduto.setCusto(existProd.getCusto());
        completeProduto.setLucro(calculateLucro(completeProduto));

        return produtoEntityPort.saveUpdate(completeProduto);
    }

    public List<String> listAllActiveMl(){
        return mercadoLivreAdapter.listActiveMlIds();
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

    private double calculateLucro(Produto produto){
        BigDecimal porcenNf = new BigDecimal(6);
        if(produto.getCsosn().equals("102")) new BigDecimal(11);
        porcenNf = porcenNf.setScale(2,  RoundingMode.HALF_UP);
        porcenNf = porcenNf.divide(new BigDecimal(100), RoundingMode.HALF_UP);

        BigDecimal nfTaxa = new BigDecimal(produto.getPrecoDesconto()).multiply(porcenNf);
        double custoTotal = produto.getCusto() + produto.getTaxaML() + produto.getCustoFrete() + nfTaxa.doubleValue();
        var lucroBig = new BigDecimal(produto.getPrecoDesconto() - custoTotal);
        lucroBig = lucroBig.setScale(2,RoundingMode.HALF_UP);
        return lucroBig.doubleValue();
    }
}
