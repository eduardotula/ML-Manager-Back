package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Order;
import org.florense.outbound.adapter.postgre.entity.OrderEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = VendaEntityMapper.class)
public interface OrderEntityMapper {

    Order toModel(OrderEntity orderEntity);
    OrderEntity toEntity(Order order);
}
