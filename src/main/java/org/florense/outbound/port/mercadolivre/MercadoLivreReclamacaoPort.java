package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Reclamacao;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;

public interface MercadoLivreReclamacaoPort {
    Reclamacao getReclamacao(String mlReclamacaoId, User user, boolean retry) throws MercadoLivreException, FailRequestRefreshTokenException;
}
