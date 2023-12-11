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

    @POST
    @Path("/")
    public ProdutoDto createProduto(@Valid ProdutoDto produtoDto){
        return produtoDtoMapper.toDto(produtoUseCase.saveUpdate(produtoDtoMapper.toModel(produtoDto)));
    }

    @POST
    @Path("/simple")
    public ProdutoDto createProdutoSimple(@Valid ProdutoDtoSimple simple){
        Produto produtoDtoSimple = Produto.builder().csosn(simple.getCsosn()).mlId(simple.getMlId()).custo(simple.getCusto()).build();
        return produtoDtoMapper.toDto(produtoUseCase.saveUpdateByMl(produtoDtoSimple));
    }

    @PUT
    @Path("/simple/{mlId}")
    public ProdutoDto updateExistProd(@PathParam("mlId") String mlID){
        return produtoDtoMapper.toDto(produtoUseCase.updateExistProd(mlID));
    }

    @GET
    @Path("/all")
    public List<ProdutoDto> listAll(){
        return produtoUseCase.listAll().stream().map(produtoDtoMapper::toDto).collect(Collectors.toList());
    }
}
