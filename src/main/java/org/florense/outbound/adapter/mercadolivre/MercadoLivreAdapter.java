package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreService;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.mapper.MercadoLivreProdutoProduto;
import org.florense.outbound.port.mercadolivre.MercadoLivrePort;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MercadoLivreAdapter implements MercadoLivrePort {

    @RestClient
    @Inject
    MercadoLivreService mercadoLivreService;

    @Inject
    MercadoLivreProdutoProduto mapper;

    @Override
    public Produto getProduto(String mlId){
        try {
            var p = mercadoLivreService.produto(mlId);
            return mapper.toProduto(p);
        } catch (UnauthorizedAcessKeyException e) {
            //TODO Gerar nova AcessKey
            getProduto(mlId);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Double getTarifas(Double preco, String categoria){
        try {
            Map<String, Object> tarifa = mercadoLivreService.getListingPrices(preco, "gold_special", categoria);
            return ((Number)tarifa.get("sale_fee_amount")).doubleValue();
        } catch (UnauthorizedAcessKeyException e) {
            //TODO Gerar nova AcessKey
            getTarifas(preco,categoria);
        }
        return null;
    }

    @Override
    public Double getFrete(String mlId, String cep) {
        try {
            Map<String, Object> frete = mercadoLivreService.getFretePrice(mlId, cep);
            List<Object> options = (List<Object>) frete.get("options");
            Map<String, Object> option = (Map<String, Object>) options.get(0);
            return ((Number) option.get("list_cost")).doubleValue();
        } catch (UnauthorizedAcessKeyException e) {
            //TODO Gerar nova AcessKey
            getFrete(mlId,cep);
        }
        return null;
    }

    @Override
    public List<String> listActiveMlIds()  {
        try {
            List<String> allActiveIds = new LinkedList<>();
            int offset = 0;
            int total = 1;

            while (offset < total){
                int limit = offset + 50;
                Map<String, Object> resp = mercadoLivreService.listMlIds("474751328","active", offset,limit);
                allActiveIds.addAll((Collection<String>) resp.get("results"));
                offset += 50;
                total = (Integer) ((Map<String, Object>)resp.get("paging")).get("total");
            }

            return allActiveIds;
        } catch (UnauthorizedAcessKeyException e) {
            //TODO Gerar nova AcessKey
            listActiveMlIds();
        }
        return null;
    }
}
