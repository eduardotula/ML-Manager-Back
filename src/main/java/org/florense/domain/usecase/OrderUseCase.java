package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.*;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.domain.model.filters.VendaFilter;
import org.florense.domain.scheduler.jobs.listallneworders.ListAllNewOrders;
import org.florense.inbound.adapter.dto.WebhookNotification;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.florense.outbound.port.postgre.VendaEntityPort;

import java.util.Objects;

@RequestScoped
public class OrderUseCase {
    @Inject
    OrderEntityPort orderEntityPort;
    @Inject
    ListAllNewOrders listAllNewOrders;
    @Inject
    VendaEntityPort vendaEntityPort;
    @Inject
    UserEntityPort userEntityPort;
    @Inject
    AnuncioEntityPort anuncioEntityPort;
    @Inject
    MercadoLivreVendaPort mercadoLivreVendaPort;

    @Transactional
    public Pagination<Order> listOrderByFilters(Long userId,OrderFilter filter){
        return orderEntityPort.listByFilters(userId, filter);
    }

    public void searchNewOrders(Long userId){
        User user = userEntityPort.findById(userId);
        if(Objects.isNull(user)) throw new IllegalArgumentException(String.format("User com id %d não encontrado",userId));
        listAllNewOrders.execute(user);
    }

    public Pagination<Venda> listVendasByAnuncio(Long anuncioId, VendaFilter filter){
        Anuncio anuncio = anuncioEntityPort.findById(anuncioId);
        if(Objects.isNull(anuncio)) throw new IllegalArgumentException(String.format("Anuncio com id %d não encontrado",anuncioId));
        return vendaEntityPort.listByAnuncio(anuncio, filter);
    }

    @Transactional
    public Order getLastOrderByUser(User user){
        return orderEntityPort.getLastOrderByUser(user);
    }

    @Transactional
    public void deleteById(Long id){
        if(Objects.isNull(orderEntityPort.findById(id))) throw new IllegalArgumentException(String.format("Order com id: %s não encontrado",id));
        orderEntityPort.deleteById(id);
    }

    @Transactional
    public void processNotification(WebhookNotification webhookNotification) throws FailRequestRefreshTokenException, MercadoLivreException {
        String orderMlId = webhookNotification.getResource().split("/")[2];
        User user = userEntityPort.findByMlIdUser(webhookNotification.getUserIdML());
        if(Objects.isNull(user)) throw new IllegalArgumentException(String.format("User com MLId %s não encontrado", webhookNotification.getUserIdML()));

        Order order = mercadoLivreVendaPort.getOrder(orderMlId,user,true);
        if(Objects.isNull(order)){

        }
    }

}
