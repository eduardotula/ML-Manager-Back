package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
public class AnuncioAdapter{

    @Inject
    AnuncioUseCase anuncioUseCase;
    @Inject
    AnuncioDtoMapper anuncioDtoMapper;

    @PUT
    @Path("/")
    public AnuncioDto updateAnuncio(@Valid AnuncioDto anuncioDto){
        return anuncioDtoMapper.toDto(anuncioUseCase.createUpdate(anuncioDtoMapper.toModel(anuncioDto)));
    }

    @POST
    @Path("/simple")
    public AnuncioDto createAnuncioSearch(@Valid AnuncioDtoSimple simple) throws FailRequestRefreshTokenException {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.createMlSearch(anuncioDtoSimple));
    }

    @PUT
    @Path("/simple")
    public AnuncioDto updateAnuncioSimple(@Valid AnuncioDtoSimple simple){
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSimple(anuncioDtoSimple));
    }

    @PUT
    @Path("/simple/{mlId}")
    public AnuncioDto searchExitProd(@PathParam("mlId") String mlID) throws FailRequestRefreshTokenException {
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSearch(mlID));
    }

    @GET
    @Path("/all")
    public List<AnuncioDto> listAll(){
        return anuncioUseCase.listAll().stream().map(anuncioDtoMapper::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/list/ml/active")
    public List<String> listAllActiveMl() throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllActiveMl();
    }

    @GET
    @Path("/list/ml/active/dife")
    public List<String> listAllActiveMlMinusRegistered() throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllActiveMlMinusRegistered();
    }

    @GET
    @Path("/{mlId}")
    public AnuncioDto findAnuncioByMlId(@PathParam("mlId") String mlId){
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlId(mlId));
    }

    @GET
    @Path("/{mlId}/search")
    public AnuncioDto findAnuncioByMlIdSearch(@PathParam("mlId") String mlId) throws FailRequestRefreshTokenException {
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlIdSearch(mlId));
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id){
        anuncioUseCase.deleteBy(id);
    }
}
