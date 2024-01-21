package org.florense.inbound.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.florense.domain.usecase.UserUseCase;
import org.florense.inbound.adapter.dto.UserDto;
import org.florense.inbound.adapter.mappers.UserDtoMapper;
import org.florense.inbound.port.AccessCodePort;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAdapter implements AccessCodePort {

    @Inject
    UserDtoMapper mapper;

    @Inject
    UserUseCase useCase;

    @POST
    @Path("")
    public UserDto create(@Valid UserDto userDto) {
        return mapper.toDto(useCase.create(mapper.toModel(userDto)));
    }

    @PUT
    @Path("")
    public UserDto update(@Valid UserDto userDto) {
        return mapper.toDto(useCase.update(mapper.toModel(userDto)));
    }

    @GET
    @Path("")
    public List<UserDto> getAll() {
        return useCase.listALl().stream().map(mapper::toDto).collect(Collectors.toList());
    }

}
