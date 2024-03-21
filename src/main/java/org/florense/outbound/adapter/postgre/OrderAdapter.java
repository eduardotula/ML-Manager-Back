package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.*;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.outbound.adapter.postgre.entity.OrderEntity;
import org.florense.outbound.adapter.postgre.mappers.OrderEntityMapper;
import org.florense.outbound.adapter.postgre.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.florense.outbound.port.postgre.OrderEntityPort;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderAdapter implements OrderEntityPort {

    @Inject
    OrderRepository repository;
    @Inject
    OrderEntityMapper mapper;

    @Override
    public Order createUpdate(Order order){
        var orderEntity = mapper.toEntity(order);
        var now = LocalDateTime.now();
        executeBeforeSave(orderEntity,now);

        return mapper.toModel(repository.save(orderEntity));
    }

    @Override
    public List<Order> createUpdateAll(List<Order> orderList){

        List<OrderEntity> orderEntitys = orderList.stream().map(mapper::toEntity).collect(Collectors.toList());
        var now = LocalDateTime.now();
        orderEntitys.forEach(orderEntity -> executeBeforeSave(orderEntity,now));
        return repository.saveAll(orderEntitys).stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void executeBeforeSave(OrderEntity orderEntity, LocalDateTime now){
        if(orderEntity.getCreatedAt() == null) orderEntity.setCreatedAt(now);
        orderEntity.getVendas().forEach(venda -> {
            if(venda.getCreatedAt() == null) venda.setCreatedAt(now);
            venda.setOrder(orderEntity);
        });
    }

    @Override
    public Pagination<Order> listByFilters(Long userId, OrderFilter filter){
        Pageable pageable = PageRequest.of(filter.getPageParam().getPage(), filter.getPageParam().getPageSize());

        var page = repository.listByFilters(filter.getOrderCreationInicial(), filter.getOrderCreationFinal(), userId, filter.getDescricao(),
                filter.getSort(), pageable);

        return new Pagination<>(filter.getPageParam().getPage(), filter.getPageParam().getPageSize(), page.getTotalPages(),
                (int) page.getTotalElements(), filter.getPageParam().getSortField(),
                filter.getPageParam().getSortType().name(), filter.getPageParam().getAvaliableSortTypes(), page.stream().map(mapper::toModel).collect(Collectors.toList()));
    }

    @Override
    public Order getLastOrderByUser(User user){
        var order = repository.findFirstByOrderByIdDesc();
        return order != null ? mapper.toModel(order) : null;
    }

    @Override
    public Order findById(Long id){
        return mapper.toModel(repository.findById(id).orElse(null));
    }

    @Override
    public Order findByOrderId(Long orderId){
        var order= repository.findByOrderId(orderId).orElse(null);
        return mapper.toModel(order);
    }
    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }

    @Override
    public List<Order> listAllOrdersByAnuncio(Anuncio anuncio){
        return repository.listOrdersByAnuncio(anuncio.getId()).stream().map(mapper::toModel).collect(Collectors.toList());
    }

}
