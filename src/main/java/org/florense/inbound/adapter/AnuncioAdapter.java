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
@Path("/anuncio")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AnuncioAdapter {

    @Inject
    AnuncioUseCase anuncioUseCase;
    @Inject
    AnuncioDtoMapper anuncioDtoMapper;

    @PUT
    @Path("/")
    public AnuncioDto updateAnuncio(@Valid AnuncioDto anuncioDto, @QueryParam("userId") @NotEmpty Long userId) {
        return anuncioDtoMapper.toDto(anuncioUseCase.createUpdate(anuncioDtoMapper.toModel(anuncioDto), userId));
    }

    @POST
    @Path("/simple")
    public AnuncioDto createAnuncioSearch(@Valid AnuncioDtoSimple simple, @QueryParam("userId") @NotEmpty Long userId) throws FailRequestRefreshTokenException {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.createMlSearch(anuncioDtoSimple, userId));
    }

    @PUT
    @Path("/simple")
    public AnuncioDto updateAnuncioSimple(@Valid AnuncioDtoSimple simple, @QueryParam("userId") @NotEmpty Long userId) {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSimple(anuncioDtoSimple, userId));
    }

    @PUT
    @Path("/simple/{mlId}")
    public AnuncioDto searchExitProd(@PathParam("mlId") String mlID, @QueryParam("userId") @NotEmpty Long userId) throws FailRequestRefreshTokenException {
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSearch(mlID, userId));
    }

    @GET
    @Path("/all")
    public List<AnuncioDto> listAll(@QueryParam("userId") @NotEmpty Long userId) {
        return anuncioUseCase.listAll(userId).stream().map(anuncioDtoMapper::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/list/ml/active")
    public List<String> listAllActiveMl(@QueryParam("userId") @NotEmpty Long userId) throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllActiveMl(userId);
    }

    @GET
    @Path("/list/ml/active/dife")
    public List<String> listAllActiveMlMinusRegistered(@QueryParam("userId") @NotEmpty Long userId) throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllActiveMlMinusRegistered(userId);
    }

    @GET
    @Path("/{mlId}")
    public AnuncioDto findAnuncioByMlId(@PathParam("mlId") String mlId, @QueryParam("userId") @NotEmpty Long userId) {
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlId(mlId, userId));
    }

    @GET
    @Path("/{mlId}/search")
    public AnuncioDto findAnuncioByMlIdSearch(@PathParam("mlId") String mlId, @QueryParam("userId") @NotEmpty Long userId) throws FailRequestRefreshTokenException {
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlIdSearch(mlId, userId));
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id) {
        anuncioUseCase.deleteBy(id);
    }
}
