package org.florense.domain.usecase;

import jakarta.enterprise.context.ApplicationScoped;
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
import org.florense.outbound.port.mercadolivre.MercadoLivreAnuncioPort;
import org.florense.outbound.port.mercadolivre.MercadoLivreVendaPort;
import org.florense.outbound.port.postgre.AnuncioEntityPort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.florense.outbound.port.postgre.VendaEntityPort;
import org.jboss.logging.Logger;

import java.util.Objects;

@ApplicationScoped
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
    @Inject
    MercadoLivreAnuncioPort mercadoLivreAnuncioPort;
    @Inject
    Logger logger;

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
        logger.infof("Inicio deleteById: id %d", id);

        if(Objects.isNull(orderEntityPort.findById(id))) throw new IllegalArgumentException(String.format("Order com id: %s não encontrado",id));
        orderEntityPort.deleteById(id);
        logger.infof("Final deleteById: id %d", id);
    }

    @Transactional
    public synchronized void processNotification(WebhookNotification webhookNotification) throws FailRequestRefreshTokenException, MercadoLivreException {
        logger.infof("Inicio processNotification: userMlId %s ", webhookNotification.getUserIdML());

        String orderMlId = webhookNotification.getResource().split("/")[2];
        User user = userEntityPort.findByMlIdUser(webhookNotification.getUserIdML());
        if(Objects.isNull(user)) throw new IllegalArgumentException(String.format("User com MLId %s não encontrado", webhookNotification.getUserIdML()));

        Order order = mercadoLivreVendaPort.getOrder(orderMlId,user,true);
        //Procura se o mesmo pedido já existe e prepara para atualizar
        var existingOrder = orderEntityPort.findByOrderId(order.getOrderId());
        if (Objects.nonNull(existingOrder)) {
            order.setId(existingOrder.getId());
            order.setCreatedAt(existingOrder.getCreatedAt());
            order.updateVendasByMatchingByMlId(existingOrder.getVendas());
        }
        //Reliza a atualização de um pedido ou cria, caso produto não existir é criado um produto temporario
        for (Venda venda : order.getVendas()) {

            double valorFrete = mercadoLivreAnuncioPort.getFrete(order.getShippingId(),user,true);

            Anuncio existingAnuncio = venda.getAnuncio();
            existingAnuncio.setPrecoDesconto((double) Math.round((venda.getPrecoDesconto() / venda.getQuantidade()) * 100) / 100);
            existingAnuncio.setCustoFrete((double) Math.round((valorFrete / venda.getQuantidade()) * 100) / 100);

            if(venda.getTaxaML() > 0) existingAnuncio.setTaxaML(venda.getTaxaML() / venda.getQuantidade());
            double lucro = Anuncio.calculateLucro(existingAnuncio);
            existingAnuncio.setLucro(lucro);

            venda.setCustoTotal(existingAnuncio.getCusto());
            venda.setCustoFreteTotal(existingAnuncio.getCustoFrete());
            venda.setImpostoTotal(existingAnuncio.getImposto());
            venda.setLucroTotal(existingAnuncio.getLucro());
            venda.setAnuncio(anuncioEntityPort.createUpdate(existingAnuncio));
        }
        orderEntityPort.createUpdate(order);
        logger.infof("Notificação processada com sucesso orderId: %s" , orderMlId);
    }
}
