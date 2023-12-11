package org.florense.inbound.adapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.usecase.ProdutoUseCase;
import org.florense.inbound.adapter.dto.ProdutoDto;
import org.florense.inbound.adapter.mappers.ProdutoDtoMapper;
import org.florense.inbound.port.ProdutoAdapterPort;

@RequestScoped
@Path("/Produto")
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
}
