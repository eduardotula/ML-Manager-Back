package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.client.MLAuthService;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.response.MLRefreshTokenResponse;
import org.florense.outbound.port.postgre.UserEntityPort;

@ApplicationScoped
public abstract class MercadoLivreAdapter {

    @Inject
    UserEntityPort userEntityPort;

    @RestClient
    @Inject
    MLAuthService mlAuthService;


    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void refreshAccessToken(String appId, String clientSecret, User user) throws FailRequestRefreshTokenException {

        MLRefreshTokenResponse refreshTokenResponse = mlAuthService.refreshToken("refresh_token",appId,clientSecret, user.getRefreshToken());
        user.setRefreshToken(refreshTokenResponse.getRefreshToken());
        user.setAccessCode(refreshTokenResponse.getAccessToken());
        userEntityPort.createUpdate(user);
    }
}
