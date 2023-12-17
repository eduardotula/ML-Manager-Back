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

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/produto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoAdapter implements ProdutoAdapterPort {

    @Inject
    ProdutoUseCase produtoUseCase;
    @Inject
    ProdutoDtoMapper produtoDtoMapper;

    @Override
    @PUT
    @Path("/")
    public ProdutoDto updateProduto(@Valid ProdutoDto produtoDto){
        return produtoDtoMapper.toDto(produtoUseCase.createUpdate(produtoDtoMapper.toModel(produtoDto)));
    }

    @Override
    @POST
    @Path("/simple")
    public ProdutoDto createProdutoSearch(@Valid ProdutoDtoSimple simple){
        Produto produtoDtoSimple = Produto.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return produtoDtoMapper.toDto(produtoUseCase.createMlSearch(produtoDtoSimple));
    }

    @Override
    @PUT
    @Path("/simple")
    public ProdutoDto updateProdutoSimple(@Valid ProdutoDtoSimple simple){
        Produto produtoDtoSimple = Produto.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return produtoDtoMapper.toDto(produtoUseCase.updateSimple(produtoDtoSimple));
    }

    @Override
    @PUT
    @Path("/simple/{mlId}")
    public ProdutoDto searchExitProd(@PathParam("mlId") String mlID){
        return produtoDtoMapper.toDto(produtoUseCase.updateSearch(mlID));
    }

    @Override
    @GET
    @Path("/all")
    public List<ProdutoDto> listAll(){
        return produtoUseCase.listAll().stream().map(produtoDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @GET
    @Path("/list/ml/active")
    public List<String> listAllActiveMl(){
        return produtoUseCase.listAllActiveMl();
    }

    @Override
    @GET
    @Path("/list/ml/active/dife")
    public List<String> listAllActiveMlMinusRegistered(){
        return produtoUseCase.listAllActiveMlMinusRegistered();
    }

    @Override
    @GET
    @Path("/{mlId}")
    public ProdutoDto findProdutoByMlId(@PathParam("mlId") String mlId){
        return produtoDtoMapper.toDto(produtoUseCase.findProdutoByMlId(mlId));
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") Long id){
        produtoUseCase.deleteBy(id);
    }
}
