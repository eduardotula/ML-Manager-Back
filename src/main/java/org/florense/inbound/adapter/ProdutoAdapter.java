package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.model.Produto;
import org.florense.domain.usecase.ProdutoUseCase;
import org.florense.inbound.adapter.dto.ProdutoDto;
import org.florense.inbound.adapter.dto.ProdutoDtoSimple;
import org.florense.inbound.adapter.mappers.ProdutoDtoMapper;
import org.florense.inbound.port.ProdutoAdapterPort;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/produto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoAdapter{

    @Inject
    ProdutoUseCase produtoUseCase;
    @Inject
    ProdutoDtoMapper produtoDtoMapper;

    @PUT
    @Path("/")
    public ProdutoDto updateProduto(@Valid ProdutoDto produtoDto){
        return produtoDtoMapper.toDto(produtoUseCase.createUpdate(produtoDtoMapper.toModel(produtoDto)));
    }

    @POST
    @Path("/simple")
    public ProdutoDto createProdutoSearch(@Valid ProdutoDtoSimple simple) throws FailRequestRefreshTokenException {
        Produto produtoDtoSimple = Produto.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return produtoDtoMapper.toDto(produtoUseCase.createMlSearch(produtoDtoSimple));
    }

    @PUT
    @Path("/simple")
    public ProdutoDto updateProdutoSimple(@Valid ProdutoDtoSimple simple){
        Produto produtoDtoSimple = Produto.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return produtoDtoMapper.toDto(produtoUseCase.updateSimple(produtoDtoSimple));
    }

    @PUT
    @Path("/simple/{mlId}")
    public ProdutoDto searchExitProd(@PathParam("mlId") String mlID) throws FailRequestRefreshTokenException {
        return produtoDtoMapper.toDto(produtoUseCase.updateSearch(mlID));
    }

    @GET
    @Path("/all")
    public List<ProdutoDto> listAll(){
        return produtoUseCase.listAll().stream().map(produtoDtoMapper::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/list/ml/active")
    public List<String> listAllActiveMl() throws FailRequestRefreshTokenException {
        return produtoUseCase.listAllActiveMl();
    }

    @GET
    @Path("/list/ml/active/dife")
    public List<String> listAllActiveMlMinusRegistered() throws FailRequestRefreshTokenException {
        return produtoUseCase.listAllActiveMlMinusRegistered();
    }

    @GET
    @Path("/{mlId}")
    public ProdutoDto findProdutoByMlId(@PathParam("mlId") String mlId){
        return produtoDtoMapper.toDto(produtoUseCase.findProdutoByMlId(mlId));
    }

    @GET
    @Path("/{mlId}/search")
    public ProdutoDto findProdutoByMlIdSearch(@PathParam("mlId") String mlId) throws FailRequestRefreshTokenException {
        return produtoDtoMapper.toDto(produtoUseCase.findProdutoByMlIdSearch(mlId));
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id){
        produtoUseCase.deleteBy(id);
    }
}
