package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.resource.spi.IllegalStateException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Anuncio;
import org.florense.domain.usecase.AnuncioUseCase;
import org.florense.inbound.adapter.dto.anuncios.AnuncioDto;
import org.florense.inbound.adapter.dto.anuncios.AnuncioDtoSimple;
import org.florense.inbound.adapter.mappers.AnuncioDtoMapper;
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

    @POST
    @Path("")
    public AnuncioDto createAnuncioSearch(@Valid AnuncioDtoSimple simple, @QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException, IllegalStateException {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.createMlSearch(anuncioDtoSimple, userId));
    }

    @PUT
    @Path("")
    public AnuncioDto updateAnuncioSimple(@Valid AnuncioDtoSimple simple, @QueryParam("user-id") Long userId) {
        Anuncio anuncioDtoSimple = Anuncio.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSimple(anuncioDtoSimple, userId));
    }

    @PUT
    @Path("{ml-id}/search")
    public AnuncioDto searchExitProd(@PathParam("ml-id") String mlID, @QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException, IllegalStateException {
        return anuncioDtoMapper.toDto(anuncioUseCase.updateSearch(mlID, userId));
    }

    @GET
    @Path("")
    public List<AnuncioDto> listAll(@QueryParam("user-id")long userId, @QueryParam("complete") @DefaultValue("false") boolean complete) {
        if(complete)
            return anuncioUseCase.listAllRegistered(userId).stream().map(anuncioDtoMapper::toDto).collect(Collectors.toList());

        return anuncioUseCase.listAll(userId).stream().map(anuncioDtoMapper::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/mercado-livre")
    public List<String> listAllAnunciosMercadoLivre(@QueryParam("user-id") Long userId,
                                                    @QueryParam("include-paused") @DefaultValue("false") boolean includePaused) throws FailRequestRefreshTokenException {
        return anuncioUseCase.listAllAnunciosMercadoLivre(userId, includePaused);
    }

    @GET
    @Path("/mlId/{ml-id}")
    public AnuncioDto findAnuncioByMlId(@PathParam("ml-id") String mlId, @QueryParam("user-id") Long userId,
                                        @QueryParam("complete") @DefaultValue("true") boolean complete) {
        if(complete)
            return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlId(mlId, userId));
        else
            return anuncioDtoMapper.toDto(anuncioUseCase.findAnyAnuncioByMlId(mlId, userId));
    }

    @GET
    @Path("/mlId/{ml-id}/ml-api")
    public AnuncioDto findAnuncioByMlIdSearch(@PathParam("ml-id") String mlId, @QueryParam("user-id") Long userId) throws FailRequestRefreshTokenException, IllegalStateException {
        return anuncioDtoMapper.toDto(anuncioUseCase.findAnuncioByMlIdSearch(mlId, userId));
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id) throws IllegalStateException {
        anuncioUseCase.deleteBy(id);
    }
}
