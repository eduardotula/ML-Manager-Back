package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.inbound.adapter.dto.WebhookNotification;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.adapter.postgre.AnuncioAdapter;
import org.florense.outbound.adapter.postgre.OrderAdapter;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.florense.outbound.adapter.postgre.repository.AnuncioRepository;
import org.florense.outbound.adapter.postgre.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RequestScoped
@Path("/scripts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScriptsAdapter {

    @Inject
    public OrderUseCase orderUseCase;

    @Inject
    public OrderRepository orderRepository;
    @Inject
    public AnuncioRepository anuncioRepository;


    @GET
    @Path("/recalculateLucro")
    @Transactional
    public void recalculateLucro(@QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException, MercadoLivreException {
        var orders = orderRepository.listOrdersByUserId(userId);
        List<AnuncioEntity> anuncioEntityList = new LinkedList<>();

        orders.forEach(orderEntity -> {
            orderEntity.getVendas().forEach(vendaEntity -> {
                var imposto = Anuncio.calculateImposto(vendaEntity.getAnuncio().getCsosn(),vendaEntity.getPrecoDesconto());
                vendaEntity.setImposto(imposto);
                vendaEntity.setLucro(Anuncio.calculateLucro(vendaEntity.getCusto(),vendaEntity.getTaxaML(),vendaEntity.getCustoFrete(),imposto,vendaEntity.getPrecoDesconto()));

                var anuncio = anuncioRepository.find(vendaEntity.getAnuncio().getId()).orElseThrow();
                anuncio.setImposto(Anuncio.calculateImposto(anuncio.getCsosn(),anuncio.getPrecoDesconto()));
                anuncioEntityList.add(anuncio);
            });
        });

        orderRepository.saveAll(orders);
        anuncioRepository.saveAll(anuncioEntityList);
    }

}
