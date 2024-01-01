package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Order;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.filters.OrderFilter;
import org.florense.outbound.adapter.postgre.mappers.OrderEntityMapper;
import org.florense.outbound.adapter.postgre.repository.OrderRepository;
import org.florense.outbound.port.postgre.OrderEntityPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
    public Pagination<Order> listByFilters(OrderFilter filter, PageParam pageParam){
        Pageable pageable = PageRequest.of(pageParam.getPage(), pageParam.getPageSize());
        Sort.Direction sortDirection = pageParam.getSortType().equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        var page = repository.listByFilters(filter.getOrderCreationInicial(), filter.getOrderCreationFinal(),
                Sort.by(sortDirection,pageParam.getSortField()), pageable);

        return new Pagination<Order>(pageParam.getPage(), pageParam.getPageSize(),page.getTotalPages(), (int)page.getTotalElements(), pageParam.getSortField(),
                pageParam.getSortType(), page.stream().map(mapper::toModel).collect(Collectors.toList()));    }

    @Override
    public Order getLastOrder(){
        return mapper.toModel(repository.findTopByOrderByIdDesc().orElseGet(null));
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
