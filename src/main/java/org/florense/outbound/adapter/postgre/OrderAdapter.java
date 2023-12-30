package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.outbound.adapter.postgre.mappers.OrderEntityMapper;
import org.florense.outbound.adapter.postgre.repository.OrderRepository;
import org.florense.outbound.port.postgre.OrderEntityPort;

import java.time.LocalDateTime;

@ApplicationScoped
public class OrderAdapter implements OrderEntityPort {

    @Inject
    OrderRepository repository;
    @Inject
    OrderEntityMapper mapper;

    @Override
    public Order saveUpdate(Order order){
        var orderEntity = mapper.toEntity(order);
        var now = LocalDateTime.now();
        if(orderEntity.getCreatedAt() == null) orderEntity.setCreatedAt(now);
        orderEntity.getVendas().forEach(venda -> {
            if(venda.getCreatedAt() == null) venda.setCreatedAt(now);
            venda.setOrder(orderEntity);
        });

        return mapper.toModel(repository.save(orderEntity));
    }

    @Override
    public Order findById(Long id){
        return mapper.toModel(repository.findById(id).orElse(null));
    }

    @Override
    public Order findByOrderId(Long orderId){
        return mapper.toModel(repository.findByOrderId(orderId).orElse(null));
    }
    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }

}
