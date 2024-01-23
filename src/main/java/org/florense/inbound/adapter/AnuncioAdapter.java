package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Anuncio;
import org.florense.domain.usecase.AnuncioUseCase;
import org.florense.inbound.adapter.dto.AnuncioDto;
import org.florense.inbound.adapter.dto.AnuncioDtoSimple;
import org.florense.inbound.adapter.mappers.AnuncioDtoMapper;
import org.florense.inbound.port.AnuncioAdapterPort;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("anuncios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AnuncioAdapter {

    @Inject
    AnuncioUseCase anuncioUseCase;
    @Inject
    AnuncioDtoMapper anuncioDtoMapper;

    @PUT
    @Path("")
    public AnuncioDto updateAnuncio(@Valid AnuncioDto anuncioDto, @QueryParam("user-id") Long userId) {
        return anuncioDtoMapper.toDto(anuncioUseCase.createUpdate(anuncioDtoMapper.toModel(anuncioDto), userId));
    }

    @POST
    @Path("/simple")
    public AnuncioDto createAnuncioSearch(@Valid AnuncioDtoSimple simple, @QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.createMlSearch(anuncioDtoSimple, userId));
    }

    @PUT
    @Path("/simple")
    public AnuncioDto updateAnuncioSimple(@Valid AnuncioDtoSimple simple, @QueryParam("user-id") Long userId) {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSimple(anuncioDtoSimple, userId));
    }

    @PUT
    @Path("{ml-id}/search")
    public AnuncioDto searchExitProd(@PathParam("ml-id") String mlID, @QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException {
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSearch(mlID, userId));
    }

    @GET
    @Path("")
    public List<AnuncioDto> listAll(@QueryParam("user-id")long userId, @QueryParam("registered") @DefaultValue("false") boolean registered) {
        if(registered)
            return anuncioUseCase.listAllRegistered(userId).stream().map(anuncioDtoMapper::toDto).collect(Collectors.toList());

        return anuncioUseCase.listAll(userId).stream().map(anuncioDtoMapper::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/status-active")
    public List<String> listAllActiveMl(@QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllActiveMl(userId);
    }

    @GET
    @Path("/list-all-active-minus-registered")
    public List<String> listAllActiveMlMinusRegistered(@QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllActiveMlMinusRegistered(userId);
    }

    @GET
    @Path("/mlId/{ml-id}")
    public AnuncioDto findAnuncioByMlId(@PathParam("ml-id") String mlId, @QueryParam("user-id") Long userId) {
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlId(mlId, userId));
    }

    @GET
    @Path("/mlId/{ml-id}/ml-api")
    public AnuncioDto findAnuncioByMlIdSearch(@PathParam("ml-id") String mlId, @QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException {
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlIdSearch(mlId, userId));
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id) {
        anuncioUseCase.deleteBy(id);
    }
}
