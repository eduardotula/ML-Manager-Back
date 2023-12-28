package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.Anuncio;
import org.florense.inbound.port.AnuncioAdapterPort;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.mercadolivre.MercadoLivrePort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequestScoped
public class AnuncioUseCase implements AnuncioAdapterPort{

    @Inject
    AnuncioEntityPort anuncioEntityPort;
    @Inject
    MercadoLivrePort mercadoLivreAdapter;

    @Override
    @Transactional
    public Anuncio createUpdate(Anuncio anuncio){
        return anuncioEntityPort.saveUpdate(anuncio);
    }


    //Cria e atualiza com mercado livre
    @Override
    @Transactional
    public Anuncio createMlSearch(Anuncio anuncio) throws FailRequestRefreshTokenException {
        var existProd = anuncioEntityPort.findByMlId(anuncio.getMlId());
        if(existProd != null) throw new IllegalArgumentException(String.format("Anuncio com id: %s já cadastrado",anuncio.getMlId()));

        Anuncio completeAnuncio = mercadoLivreAdapter.getAnuncio(anuncio.getMlId(), true);
        completeAnuncio.setTaxaML(mercadoLivreAdapter.getTarifas(completeAnuncio.getPrecoDesconto(), completeAnuncio.getCategoria() , true));
        if(completeAnuncio.getPrecoDesconto() >= 80)
            completeAnuncio.setCustoFrete(mercadoLivreAdapter.getFrete(completeAnuncio.getMlId(),"06950000" , true));
        else completeAnuncio.setCustoFrete(0.0);

        completeAnuncio.setCusto(anuncio.getCusto());
        completeAnuncio.setCsosn(anuncio.getCsosn());
        completeAnuncio.setLucro(calculateLucro(completeAnuncio));

        return anuncioEntityPort.saveUpdate(completeAnuncio);
    }

    //Somente atualiza um anuncio
    @Override
    @Transactional
    public Anuncio updateSimple(Anuncio anuncio){
        var existProd = anuncioEntityPort.findByMlId(anuncio.getMlId());
        if(Objects.isNull(existProd)) throw new IllegalArgumentException(String.format("Anuncio com id %s não encontrado", anuncio.getMlId()));
        existProd.setCsosn(anuncio.getCsosn());
        existProd.setCusto(anuncio.getCusto());
        existProd.setLucro(calculateLucro(existProd));

        return anuncioEntityPort.saveUpdate(existProd);
    }

    //Atualiza dados somente do mercado livre
    @Override
    @Transactional
    public Anuncio updateSearch(String mlId) throws FailRequestRefreshTokenException {

         var existProd = anuncioEntityPort.findByMlId(mlId);
        if(Objects.isNull(existProd)){
            throw new IllegalArgumentException(String.format("Anuncio com mlId: %s não encontrado", mlId ));
        }

        Anuncio completeAnuncio = mercadoLivreAdapter.getAnuncio(mlId, true);
        completeAnuncio.setTaxaML(mercadoLivreAdapter.getTarifas(completeAnuncio.getPrecoDesconto(), completeAnuncio.getCategoria(), true));
        if(completeAnuncio.getPrecoDesconto() >= 80)
            completeAnuncio.setCustoFrete(mercadoLivreAdapter.getFrete(completeAnuncio.getMlId(),"06950000", true));
        else completeAnuncio.setCustoFrete(0.0);
        completeAnuncio.update(existProd);
        completeAnuncio.setCsosn(existProd.getCsosn());
        completeAnuncio.setCusto(existProd.getCusto());
        completeAnuncio.setLucro(calculateLucro(completeAnuncio));

        return anuncioEntityPort.saveUpdate(completeAnuncio);
    }

    @Override
    @Transactional
    public List<String> listAllActiveMl() throws FailRequestRefreshTokenException {
        return mercadoLivreAdapter.listActiveMlIds(true);
    }

    @Override
    @Transactional
    public List<String> listAllActiveMlMinusRegistered() throws FailRequestRefreshTokenException {
        List<String> actives = mercadoLivreAdapter.listActiveMlIds(true);
        Set<String> registeredIds = anuncioEntityPort.listAll()
                .stream()
                .map(Anuncio::getMlId)
                .collect(Collectors.toSet());

        return actives.stream()
                .filter(mlId -> !registeredIds.contains(mlId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Anuncio findAnuncioByMlId(String mlId){
        return anuncioEntityPort.findByMlId(mlId);
    }

    @Override
    @Transactional
    public Anuncio findAnuncioByMlIdSearch(String mlId) throws FailRequestRefreshTokenException {
        return mercadoLivreAdapter.getAnuncio(mlId,true);
    }

    @Override
    @Transactional
    public List<Anuncio> listAll(){
        return anuncioEntityPort.listAll();
    }

    @Override
    @Transactional
    public void deleteBy(Long id){
        if(Objects.isNull(anuncioEntityPort.findById(id)))
            throw new IllegalArgumentException(String.format("Anuncio com id %s não encontrado", id));

        try{
            anuncioEntityPort.deleteById(id);
        }catch (Exception e){
            throw new IllegalStateException(String.format("Falha ao apagar anuncio com id: %s",id));
        }
    }

    private double calculateLucro(Anuncio anuncio){
        BigDecimal porcenNf = new BigDecimal(6);
        if(anuncio.getCsosn().equals("102")) new BigDecimal(11);
        porcenNf = porcenNf.setScale(2,  RoundingMode.HALF_UP);
        porcenNf = porcenNf.divide(new BigDecimal(100), RoundingMode.HALF_UP);

        var precoDesconto = BigDecimal.valueOf(anuncio.getPrecoDesconto()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nfTaxa = precoDesconto.multiply(porcenNf);
        double custoTotal = anuncio.getCusto() + anuncio.getTaxaML() + anuncio.getCustoFrete() + nfTaxa.doubleValue();
        var lucroBig = new BigDecimal(anuncio.getPrecoDesconto() - custoTotal);
        lucroBig = lucroBig.setScale(2,RoundingMode.HALF_UP);
        return lucroBig.doubleValue();
    }
}
