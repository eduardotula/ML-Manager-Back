package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.AccessCode;
import org.florense.domain.model.Produto;
import org.florense.domain.usecase.AccessCodeUseCase;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreService;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.mapper.MercadoLivreProdutoProduto;
import org.florense.outbound.adapter.mercadolivre.response.MLRefreshTokenResponse;
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
    AccessCodeUseCase accessCodeUseCase;

    @Inject
    MercadoLivreProdutoProduto mapper;

    @ConfigProperty(name = "quarkus.rest-client.ml-api.app-id")
    String appId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.secret")
    String clientSecret;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.user-id")
    String userId;


    @Override
    public Produto getProduto(String mlId, boolean retry) throws FailRequestRefreshTokenException {
        try {
            var p = mercadoLivreService.produto(mlId);
            return mapper.toProduto(p);
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken();
                if(retry) getProduto(mlId,false);
            }
        }
        return null;
    }

    @Override
    public Double getTarifas(Double preco, String categoria, boolean retry) throws FailRequestRefreshTokenException {
        try {
            Map<String, Object> tarifa = mercadoLivreService.getListingPrices(preco, "gold_special", categoria);
            return ((Number)tarifa.get("sale_fee_amount")).doubleValue();
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken();
                if(retry) getTarifas(preco, categoria,false);
            }
        }
        return null;
    }

    @Override
    public Double getFrete(String mlId, String cep, boolean retry) throws FailRequestRefreshTokenException {
        try {
            Map<String, Object> frete = mercadoLivreService.getFretePrice(mlId, cep);
            List<Object> options = (List<Object>) frete.get("options");
            Map<String, Object> option = (Map<String, Object>) options.get(0);
            return ((Number) option.get("list_cost")).doubleValue();
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken();
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
                int limit = offset + 50;
                Map<String, Object> resp = mercadoLivreService.listMlIds(userId,"active", offset,limit);
                allActiveIds.addAll((Collection<String>) resp.get("results"));
                offset += 50;
                total = (Integer) ((Map<String, Object>)resp.get("paging")).get("total");
            }

            return allActiveIds;
        } catch (RuntimeException e) {
            if(e.getCause() instanceof UnauthorizedAcessKeyException){
                refreshAccessToken();
                if(retry) listActiveMlIds(false);
            }
        }
        return null;
    }
    private void refreshAccessToken() throws FailRequestRefreshTokenException {
        AccessCode accessCode = accessCodeUseCase.get();

        MLRefreshTokenResponse refreshTokenResponse = mercadoLivreService.refreshToken("refresh_token",appId,clientSecret, accessCode.getRefreshToken());
        AccessCode newAccessCode = new AccessCode(null, refreshTokenResponse.getAccessToken(), refreshTokenResponse.getRefreshToken(), null);
        accessCodeUseCase.create(newAccessCode);
        accessCodeUseCase.deleteById(accessCode.getId());
    }
}
