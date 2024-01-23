package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.User;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.outbound.adapter.postgre.entity.OrderEntity;
import org.florense.outbound.adapter.postgre.mappers.OrderEntityMapper;
import org.florense.outbound.adapter.postgre.repository.OrderRepository;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
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
        if(orderEntity.getCreatedAt() == null) orderEntity.setCreatedAt(now);
        orderEntity.getVendas().forEach(venda -> {
            if(venda.getCreatedAt() == null) venda.setCreatedAt(now);
            venda.setOrder(orderEntity);
        });

        return mapper.toModel(repository.save(orderEntity));
    }

    @Override
    public List<Order> createUpdateAll(List<Order> orderList){

        List<OrderEntity> orderEntitys = orderList.stream().map(mapper::toEntity).collect(Collectors.toList());
        var now = LocalDateTime.now();
        orderEntitys.forEach(orderEntity -> {
            if(orderEntity.getCreatedAt() == null) orderEntity.setCreatedAt(now);
            orderEntity.getVendas().forEach(venda -> {
                if(venda.getCreatedAt() == null) venda.setCreatedAt(now);
                venda.setOrder(orderEntity);
            });
        });
        return repository.saveAll(orderEntitys).stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Pagination<Order> listByFilters(Long userId, OrderFilter filter, PageParam pageParam){
        Pageable pageable = PageRequest.of(pageParam.getPage(), pageParam.getPageSize());
        Sort.Direction sortDirection = pageParam.getSortType().equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if(pageParam.getSortField() == "id") pageParam.setSortField(String.format("o.%s",pageParam.getSortField()));
        var page = repository.listByFilters(filter.getOrderCreationInicial(), filter.getOrderCreationFinal(), userId,
                Sort.by(sortDirection,pageParam.getSortField()), pageable);

        return new Pagination<Order>(pageParam.getPage(), pageParam.getPageSize(),page.getTotalPages(), (int)page.getTotalElements(), pageParam.getSortField(),
                pageParam.getSortType(), page.stream().map(mapper::toModel).collect(Collectors.toList()));
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
        return mapper.toModel(repository.findByOrderId(orderId).orElse(null));
    }
    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }

}
