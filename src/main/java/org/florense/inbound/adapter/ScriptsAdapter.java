package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Venda;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.inbound.adapter.dto.WebhookNotification;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;
import org.florense.outbound.adapter.postgre.AnuncioAdapter;
import org.florense.outbound.adapter.postgre.OrderAdapter;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.florense.outbound.adapter.postgre.entity.VendaEntity;
import org.florense.outbound.adapter.postgre.mappers.OrderEntityMapper;
import org.florense.outbound.adapter.postgre.mappers.VendaEntityMapper;
import org.florense.outbound.adapter.postgre.repository.AnuncioRepository;
import org.florense.outbound.adapter.postgre.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/scripts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScriptsAdapter {


    @Inject
    OrderAdapter orderAdapter;

    @Inject
    public OrderRepository orderRepository;
    @Inject
    public AnuncioRepository anuncioRepository;

    @Inject
    OrderEntityMapper orderEntityMapper;



    @GET
    @Path("/recalculateLucro")
    @Transactional
    public void recalculateLucro(@QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException, MercadoLivreException {
        var orders = orderRepository.listOrdersByUserId(userId).stream().map(orderEntityMapper::toModel).collect(Collectors.toList());



        orders.forEach(order -> {

            List<Venda> newVendas = new ArrayList<>();
            order.getVendas().forEach(venda -> {

                if(venda.getAnuncio().isComplete()){
                    venda.setCustoTotal(venda.getCusto());
                    venda.setTaxaMLTotal(venda.getTaxaML());
                    venda.setImposto(Anuncio.calculateImposto(venda.getAnuncio().getCsosn(),venda.getPrecoDesconto()));
                    venda.setLucro(Anuncio.calculateLucro(venda.getCusto(),venda.getTaxaML(),venda.getCustoFrete(),venda.getImposto(),venda.getPrecoDesconto()));
                    newVendas.add(venda);
                }
            });
            order.setVendas(newVendas);
        });
        orderAdapter.createUpdateAll(orders);
    }



}
