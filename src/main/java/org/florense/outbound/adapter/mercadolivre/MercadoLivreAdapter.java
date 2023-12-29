package org.florense.outbound.adapter.mercadolivre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.florense.domain.model.AccessCode;
import org.florense.domain.usecase.AccessCodeUseCase;
import org.florense.outbound.adapter.mercadolivre.client.MLAuthService;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.response.MLRefreshTokenResponse;

@ApplicationScoped
public abstract class MercadoLivreAdapter {

    @Inject
    AccessCodeUseCase accessCodeUseCase;

    @RestClient
    @Inject
    MLAuthService mlAuthService;


    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void refreshAccessToken(String appId, String clientSecret) throws FailRequestRefreshTokenException {
        AccessCode accessCode = accessCodeUseCase.get();

        MLRefreshTokenResponse refreshTokenResponse = mlAuthService.refreshToken("refresh_token",appId,clientSecret, accessCode.getRefreshToken());
        AccessCode newAccessCode = new AccessCode(null, refreshTokenResponse.getAccessToken(), refreshTokenResponse.getRefreshToken(), null);
        accessCodeUseCase.create(newAccessCode);
        accessCodeUseCase.deleteById(accessCode.getId());
    }
}
