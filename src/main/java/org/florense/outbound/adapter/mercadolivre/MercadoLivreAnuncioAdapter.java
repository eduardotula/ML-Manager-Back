package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.client.MercadoLivreAnuncioClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MLErrorTypesEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
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
    public Anuncio getAnuncio(String mlId, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException {
        try {
            var p = mercadoLivreAnuncioClient.anuncio(mlId, user.getAccessCode());
            return mapper.toAnuncio(p);
        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getAnuncio(mlId, user, false);
                }
            }
            throw new MercadoLivreException(String.format("Falha ao obter anuncio %s", mlId), "getAnuncio",MLErrorTypesEnum.DEFAULT, e);
        }
    }

    @Override
    public double getTarifas(double preco, String categoria, ListingTypeEnum typeEnum, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException {
        try {
            Map<String, Object> tarifa = mercadoLivreAnuncioClient.getListingPrices(preco, typeEnum.getValue(), categoria, user.getAccessCode());
            return ((Number) tarifa.get("sale_fee_amount")).doubleValue();
        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getTarifas(preco, categoria, typeEnum, user, false);
                }
            }
            throw new MercadoLivreException(String.format("Falha ao obter tarifas categoria %s", categoria), "getTarifas",MLErrorTypesEnum.DEFAULT, e);
        }
    }

    @Override
    public double getFrete(String mlId, String anuncioStatus, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException {
        try {
            if(anuncioStatus.equalsIgnoreCase("paused")) throw new MercadoLivreException("Anuncio pausado", "getFrete", MLErrorTypesEnum.DEFAULT, null);

            Map<String, Object> frete = mercadoLivreAnuncioClient.getFretePrice(mlId, user.getCep(), user.getAccessCode());
            List<Object> options = (List<Object>) frete.get("options");
            Map<String, Object> option = (Map<String, Object>) options.get(0);
            return ((Number) option.get("list_cost")).doubleValue();
        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getFrete(mlId, anuncioStatus,user, false);
                }
                //Caso produto esteja no full e desativado
            }else if(e.getDetail().equalsIgnoreCase("non available fbm origins for these items")){
                throw new MercadoLivreException("Falha ao obter frete de produto Full", "getFrete", MLErrorTypesEnum.FRETE_VALUE,e);
            }
            throw new MercadoLivreException(String.format("Falha ao obter frete mlId %s", mlId), "getFrete", MLErrorTypesEnum.DEFAULT, e);
        }
    }
    @Override
    public double getFrete(Long shippingId, User user, boolean retry) throws FailRequestRefreshTokenException, MercadoLivreException{
        try{
            Map<String, Object> response = mercadoLivreAnuncioClient.getFretePriceByShippingId(shippingId.toString(), user.getAccessCode());
            List<Object> options = (List<Object>) response.get("senders");
            Map<String, Object> senders = (Map<String, Object>) options.get(0);
            return ((Number) senders.get("cost")).doubleValue();

        }catch (MercadoLivreClientException e){
            refreshAccessToken(appId, clientSecret, user);
            if (retry) {
                return getFrete(shippingId,user, false);
            }
            throw new MercadoLivreException(String.format("Falha ao obter frete shippingId %s", shippingId), "getFrete", MLErrorTypesEnum.DEFAULT, e);
        }
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
        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return listAllAnunciosMercadoLivre(user, includePaused,false);
                }
            }
        }
        return new ArrayList<>();
    }


}
