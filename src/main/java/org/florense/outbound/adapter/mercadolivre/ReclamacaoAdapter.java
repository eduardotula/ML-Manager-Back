package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.Reclamacao;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.client.ClaimsClient;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MLErrorTypesEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.adapter.mercadolivre.mapper.ClaimMapper;
import org.florense.outbound.port.mercadolivre.MercadoLivreReclamacaoPort;

@ApplicationScoped
public class ReclamacaoAdapter extends MercadoLivreAdapter implements MercadoLivreReclamacaoPort {


    @RestClient
    @Inject
    ClaimsClient client;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.app-id")
    String appId;
    @ConfigProperty(name = "quarkus.rest-client.ml-api.secret")
    String clientSecret;
    @Inject
    ClaimMapper claimMapper;


    @Override
    public Reclamacao getReclamacao(String mlReclamacaoId, User user, boolean retry) throws MercadoLivreException, FailRequestRefreshTokenException {
        try {
            var o = client.claim(mlReclamacaoId, user.getAccessCode());
            return this.claimMapper.toModel(o.data.get(0));
        } catch (MercadoLivreClientException e) {
            if (e.isRefreshToken()) {
                refreshAccessToken(appId, clientSecret, user);
                if (retry) {
                    return getReclamacao(mlReclamacaoId, user, false);
                }
            }
            logger.error("Falha ao obter reclamacao", e);
            throw new MercadoLivreException(String.format("Falha ao obter reclamacao %s", mlReclamacaoId), "getReclamacao", MLErrorTypesEnum.DEFAULT, e);
        }
    }
}
