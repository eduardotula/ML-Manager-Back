package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.domain.util.Json;
import org.florense.inbound.adapter.dto.OrderDto;
import org.florense.inbound.adapter.mappers.OrderDtoMapper;
import org.florense.inbound.responses.PaginationResponse;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

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
    Json mapper;

//    @PUT
//    @Path("/")
//    public OrderDto updateAnuncio(@Valid AnuncioDto anuncioDto){
//        return orderDtoMapper.toDto(orderUseCase.createUpdate(orderDtoMapper.toModel(anuncioDto)));
//    }

    @GET
    @Path("")
    public PaginationResponse<OrderDto> listByFilters(@QueryParam("page") @DefaultValue("0") Integer page,
                                                      @QueryParam("pageSize") @DefaultValue("50") Integer pageSize,
                                                      @QueryParam("sortField") @DefaultValue("id") String sortField,
                                                      @QueryParam("sortType") @DefaultValue("ASC") String sortType,
                                                      @QueryParam("dataInicial") LocalDateTime dataInicial,
                                                      @QueryParam("dataFinal") LocalDateTime dataFinal) {
        PageParam pageParam = new PageParam(page, pageSize, sortField, sortType);
        OrderFilter filter = new OrderFilter(dataInicial, dataFinal);
        Pagination<Order> pagination = orderUseCase.listOrderByFilters(filter, pageParam);
        Pagination<OrderDto> paginationDto = pagination.to(orderDtoMapper::toDto);
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
