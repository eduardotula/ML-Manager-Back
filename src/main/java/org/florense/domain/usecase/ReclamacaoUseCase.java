package org.florense.domain.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.Order;
import org.florense.domain.model.Reclamacao;
import org.florense.domain.model.User;
import org.florense.inbound.adapter.dto.WebhookNotification;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.port.mercadolivre.MercadoLivreReclamacaoPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.florense.outbound.port.postgre.ReclamacaoEntityPort;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.jboss.logging.Logger;

import java.io.ObjectStreamClass;
import java.util.Objects;

@ApplicationScoped
public class ReclamacaoUseCase {

    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    ReclamacaoEntityPort reclamacaoEntityPort;
    @Inject
    MercadoLivreReclamacaoPort reclamacaoPort;
    @Inject
    UserEntityPort userEntityPort;
    @Inject
    Logger logger;

    @Transactional
    public synchronized void processNotification(WebhookNotification webhookNotification) throws FailRequestRefreshTokenException, MercadoLivreException {
        logger.infof("Final processNotification: userMlId %s", webhookNotification.getUserIdML());

        String mlReclamacaoId = webhookNotification.getResource().split("/")[3];
        User user = userEntityPort.findByMlIdUser(webhookNotification.getUserIdML());
        if(Objects.isNull(user)) throw new IllegalArgumentException(String.format("User com MLId %s não encontrado", webhookNotification.getUserIdML()));

        Reclamacao reclamacao = reclamacaoPort.getReclamacao(mlReclamacaoId, user, true);
        Order order = orderEntityPort.findByOrderId(reclamacao.getResourceId());
        if(Objects.isNull(order)){
            logger.infof("Notificação recebida mas ordem com mlId %s não encontrado",reclamacao.getResourceId());
            return;
        }

        Reclamacao existingReclamacao = reclamacaoEntityPort.findByReclamacaoId(Long.parseLong(mlReclamacaoId));
        if(Objects.nonNull(existingReclamacao)){
            reclamacao.setId(existingReclamacao.getId());
            reclamacao.setCreatedAt(existingReclamacao.getCreatedAt());
        }

        reclamacao.setOrderId(order.getId());
        reclamacaoEntityPort.createUpdate(reclamacao);
        logger.infof("Notificação processada com sucesso mlReclamacaoId: %s" , mlReclamacaoId);
    }


}
