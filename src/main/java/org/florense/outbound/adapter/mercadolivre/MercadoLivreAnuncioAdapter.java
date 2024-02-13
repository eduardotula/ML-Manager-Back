package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.resource.spi.IllegalStateException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreAnuncioClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;
import org.florense.outbound.adapter.mercadolivre.mapper.MercadoLivreProdutoAnuncio;
import org.florense.domain.model.ListingTypeEnum;
import org.florense.outbound.port.mercadolivre.MercadoLivreAnuncioPort;

import java.util.*;

@ApplicationScoped
public class MercadoLivreAnuncioAdapter extends MercadoLivreAdapter implements MercadoLivreAnuncioPort {

    @RestClient
    @Inject
    MercadoLivreAnuncioClient mercadoLivreAnuncioClient;

    @Inject
    MercadoLivreProdutoAnuncio mapper;

    @ConfigProperty(name = "quarkus.rest-client.ml-api.app-id")
    String appId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.secret")
    String clientSecret;
    private static final int BATCH_SIZE = 50;

    @Override
    public Anuncio getAnuncio(String mlId, User user, boolean retry) throws FailRequestRefreshTokenException, IllegalStateException {
        try {
            var p = mercadoLivreAnuncioClient.anuncio(mlId, user.getAccessCode());
            return mapper.toAnuncio(p);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof UnauthorizedAcessKeyException) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getAnuncio(mlId, user, false);
                }
            }
        }
        throw new IllegalStateException(String.format("Falha ao obter anuncio %s", mlId));
    }

    @Override
    public double getTarifas(double preco, String categoria, ListingTypeEnum typeEnum, User user, boolean retry) throws FailRequestRefreshTokenException, IllegalStateException {
        try {
            Map<String, Object> tarifa = mercadoLivreAnuncioClient.getListingPrices(preco, typeEnum.getValue(), categoria, user.getAccessCode());
            return ((Number) tarifa.get("sale_fee_amount")).doubleValue();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof UnauthorizedAcessKeyException) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getTarifas(preco, categoria, typeEnum, user, false);
                }
            }
        }
        throw new IllegalStateException(String.format("Falha ao obter tarifas categoria %s", categoria));
    }

    @Override
    public double getFrete(String mlId, User user, boolean retry) throws FailRequestRefreshTokenException, IllegalStateException {
        try {
            Map<String, Object> frete = mercadoLivreAnuncioClient.getFretePrice(mlId, user.getCep(), user.getAccessCode());
            List<Object> options = (List<Object>) frete.get("options");
            Map<String, Object> option = (Map<String, Object>) options.get(0);
            return ((Number) option.get("list_cost")).doubleValue();
        } catch (Exception e) {
            if (e.getCause() instanceof UnauthorizedAcessKeyException) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getFrete(mlId,user, false);
                }
            }
        }
        throw new IllegalStateException(String.format("Falha ao obter frete mlId %s", mlId));
    }

    @Override
    public List<String> listAllAnunciosMercadoLivre(User user, boolean includePaused, boolean retry) throws FailRequestRefreshTokenException {
        try {
            List<String> allActiveIds = new LinkedList<>();
            int offset = 0;
            int total = 1;
            String status = includePaused ? "" : "active";
            while (offset < total) {
                Map<String, Object> resp = mercadoLivreAnuncioClient.listMlIds(user.getUserIdML(), status, offset, user.getAccessCode());
                allActiveIds.addAll((Collection<String>) resp.get("results"));
                offset += BATCH_SIZE;
                total = ((Number) ((Map<String, Object>) resp.get("paging")).get("total")).intValue();
            }

            return allActiveIds;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof UnauthorizedAcessKeyException) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return listAllAnunciosMercadoLivre(user, includePaused,false);
                }
            }
        }
        return new ArrayList<>();
    }


}
