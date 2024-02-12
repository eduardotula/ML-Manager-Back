package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.Venda;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.domain.model.filters.VendaFilter;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.domain.util.Json;
import org.florense.inbound.adapter.dto.OrderDto;
import org.florense.inbound.adapter.dto.VendaDto;
import org.florense.inbound.adapter.mappers.OrderDtoMapper;
import org.florense.inbound.adapter.mappers.VendaDtoMapper;
import org.florense.inbound.responses.PaginationResponse;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderAdapter {

    @Inject
    OrderUseCase orderUseCase;
    @Inject
    OrderDtoMapper orderDtoMapper;
    @Inject
    VendaDtoMapper vendaDtoMapper;
    @Inject
    Json mapper;


    @GET
    @Path("")
    public PaginationResponse<OrderDto> listByFilters(@QueryParam("page") @DefaultValue("0") int page,
                                                      @QueryParam("pageSize") @DefaultValue("50") int pageSize,
                                                      @QueryParam("sortField") @DefaultValue("id") String sortField,
                                                      @QueryParam("sortType") @DefaultValue("ASC") String sortType,
                                                      @QueryParam("dataInicial") LocalDateTime dataInicial,
                                                      @QueryParam("dataFinal") LocalDateTime dataFinal,
                                                      @QueryParam("descricao") String descricao,
                                                      @QueryParam("user-id") @NotNull(message = "user-id não informado") Long userId) {
        PageParam pageParam = new PageParam(page, pageSize, sortField, sortType, OrderFilter.getAvaliableSortTypes());
        OrderFilter filter = new OrderFilter(dataInicial, dataFinal, descricao, pageParam);
        Pagination<Order> pagination = orderUseCase.listOrderByFilters(userId, filter);
        Pagination<OrderDto> paginationDto = pagination.to(orderDtoMapper::toDto);
        var respo = new PaginationResponse<>(paginationDto, filter);
        respo.getMetaInfo().setSearch(mapper.toMap(filter));
        return respo;
    }

    @PATCH
    @Path("/users/{user-id}/refresh")
    @ResponseStatus(200)
    public void refreshOrders(@PathParam("user-id") @NotNull(message = "userId não infomado") Long userId) {
        orderUseCase.searchNewOrders(userId);
    }

    @GET
    @Path("/vendas/anuncios/{id}")
    public PaginationResponse<VendaDto> listVendasAnuncioByFilters(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("50") int pageSize,
            @QueryParam("sortField") @DefaultValue("id") String sortField,
            @QueryParam("sortType") @DefaultValue("ASC") String sortType,
            @QueryParam("dataInicial") LocalDateTime dataInicial,
            @QueryParam("dataFinal") LocalDateTime dataFinal,
            @QueryParam("include-cancelled") @DefaultValue("false") boolean includeCancelled,
            @PathParam("id") Long anuncioId) {
        PageParam pageParam = new PageParam(page, pageSize, sortField, sortType, VendaFilter.getAvaliableSortTypes());
        VendaFilter filter = new VendaFilter(dataInicial, dataFinal, includeCancelled, pageParam);
        Pagination<Venda> pagination = orderUseCase.listVendasByAnuncio(anuncioId, filter);
        Pagination<VendaDto> paginationDto = pagination.to(vendaDtoMapper::toDto);
        var respo = new PaginationResponse<>(paginationDto, filter);
        respo.getMetaInfo().setSearch(mapper.toMap(filter));
        return respo;
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id) {
        orderUseCase.deleteById(id);
    }
}
