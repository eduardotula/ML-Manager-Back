package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.AccessCode;
import org.florense.domain.model.Anuncio;
import org.florense.domain.usecase.AccessCodeUseCase;
import org.florense.outbound.adapter.mercadolivre.client.MLAuthService;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreAnuncioService;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.mapper.MercadoLivreProdutoAnuncio;
import org.florense.outbound.adapter.mercadolivre.response.MLRefreshTokenResponse;
import org.florense.outbound.port.mercadolivre.MercadoLivreAnuncioPort;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MercadoLivreAnuncioAdapter extends MercadoLivreAdapter implements MercadoLivreAnuncioPort {

    @RestClient
    @Inject
    MercadoLivreAnuncioService mercadoLivreAnuncioService;

    @Inject
    MercadoLivreProdutoAnuncio mapper;

    @ConfigProperty(name = "quarkus.rest-client.ml-api.user-id")
    String userId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.app-id")
    String appId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.secret")
    String clientSecret;


    @Override
    public Anuncio getAnuncio(String mlId, boolean retry) throws FailRequestRefreshTokenException {
        try {
            var p = mercadoLivreAnuncioService.anuncio(mlId);
            return mapper.toAnuncio(p);
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken(appId, clientSecret);
                if(retry) getAnuncio(mlId,false);
            }
        }
        return null;
    }

    @Override
    public Double getTarifas(Double preco, String categoria, boolean retry) throws FailRequestRefreshTokenException {
        try {
            Map<String, Object> tarifa = mercadoLivreAnuncioService.getListingPrices(preco, "gold_special", categoria);
            return ((Number)tarifa.get("sale_fee_amount")).doubleValue();
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken(appId, clientSecret);
                if(retry) getTarifas(preco, categoria,false);
            }
        }
        return null;
    }

    @Override
    public Double getFrete(String mlId, String cep, boolean retry) throws FailRequestRefreshTokenException {
        try {
            Map<String, Object> frete = mercadoLivreAnuncioService.getFretePrice(mlId, cep);
            List<Object> options = (List<Object>) frete.get("options");
            Map<String, Object> option = (Map<String, Object>) options.get(0);
            return ((Number) option.get("list_cost")).doubleValue();
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken(appId, clientSecret);
                if(retry) getFrete(mlId,cep,false);
            }
        }
        return null;
    }

    @Override
    public List<String> listActiveMlIds(boolean retry) throws FailRequestRefreshTokenException {
        try {
            List<String> allActiveIds = new LinkedList<>();
            int offset = 0;
            int total = 1;

            while (offset < total){
                Map<String, Object> resp = mercadoLivreAnuncioService.listMlIds(userId,"active", offset);
                allActiveIds.addAll((Collection<String>) resp.get("results"));
                offset += 50;
                total = (Integer) ((Map<String, Object>)resp.get("paging")).get("total");
            }

            return allActiveIds;
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken(appId, clientSecret);
                if(retry) listActiveMlIds(false);
            }
        }
        return null;
    }


}
