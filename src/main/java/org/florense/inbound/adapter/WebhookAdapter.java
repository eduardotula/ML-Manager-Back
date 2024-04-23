package org.florense.inbound.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.enums.NotificationTopicEnum;
import org.florense.domain.usecase.AnuncioUseCase;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.inbound.adapter.dto.WebhookNotification;
import org.jboss.resteasy.reactive.ResponseStatus;

@ApplicationScoped
@Path("webhook")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WebhookAdapter {

    @Inject
    OrderUseCase orderUseCase;
    @Inject
    AnuncioUseCase anuncioUseCase;

    @POST
    @ResponseStatus(200)
    public void mercadoLivreNotification(WebhookNotification webhookNotification){
        String topic = webhookNotification.getTopic();

        if (topic.equals(NotificationTopicEnum.ORDERS.getValue())) orderUseCase.processNotification(webhookNotification);
        else if(topic.equals(NotificationTopicEnum.ITEMS.getValue())) anuncioUseCase.processNotification(webhookNotification);
    }

}
