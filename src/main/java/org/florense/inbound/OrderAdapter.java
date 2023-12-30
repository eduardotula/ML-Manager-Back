package org.florense.inbound;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Anuncio;
import org.florense.domain.usecase.OrderUseCase;
import org.florense.inbound.adapter.dto.AnuncioDto;
import org.florense.inbound.adapter.dto.AnuncioDtoSimple;
import org.florense.inbound.adapter.dto.OrderDto;
import org.florense.inbound.adapter.mappers.OrderDtoMapper;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderAdapter {

    @Inject
    OrderUseCase orderUseCase;
    @Inject
    OrderDtoMapper orderDtoMapper;

//    @PUT
//    @Path("/")
//    public AnuncioDto updateAnuncio(@Valid AnuncioDto anuncioDto){
//        return orderDtoMapper.toDto(orderUseCase.createUpdate(orderDtoMapper.toModel(anuncioDto)));
//    }


    @PUT
    @Path("/search")
    public List<OrderDto> searchAllOrders() throws FailRequestRefreshTokenException {
        return orderUseCase.searchAllOrders().stream().map(orderDtoMapper::toDto).collect(Collectors.toList());
    }

//    @GET
//    @Path("/all")
//    public List<AnuncioDto> listAll(){
//        return orderUseCase.listAll().stream().map(orderDtoMapper::toDto).collect(Collectors.toList());
//    }


    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id){
        orderUseCase.deleteById(id);
    }
}
