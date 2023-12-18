package org.florense.inbound.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.usecase.AccessCodeUseCase;
import org.florense.inbound.adapter.dto.AccessCodeDto;
import org.florense.inbound.adapter.mappers.AccessCodeDtoMapper;
import org.florense.inbound.port.AccessCodePort;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/accessCode")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccessCodeAdapter implements AccessCodePort {

    @Inject
    AccessCodeDtoMapper mapper;

    @Inject
    AccessCodeUseCase useCase;

    @POST
    @Path("")
    public AccessCodeDto create(@Valid AccessCodeDto accessCodeDto){
        return mapper.toDto(useCase.create(mapper.toModel(accessCodeDto)));
    }

    @PUT
    @Path("")
    public AccessCodeDto update(@Valid AccessCodeDto accessCodeDto){
        return mapper.toDto(useCase.update(mapper.toModel(accessCodeDto)));
    }

    @GET
    @Path("/all")
    public List<AccessCodeDto> getAll(){
        return useCase.listALl().stream().map(mapper::toDto).collect(Collectors.toList());
    }

}
