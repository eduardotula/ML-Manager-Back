package org.florense.inbound.adapter.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Order;
import org.florense.inbound.adapter.dto.OrderDto;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = VendaDtoMapper.class)
public interface OrderDtoMapper {

    OrderDto toDto(Order order);

    Order toModel(OrderDto orderDto);
}
