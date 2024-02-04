package org.florense.outbound.adapter.postgre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.*;
import org.florense.domain.model.filters.VendaFilter;
import org.florense.outbound.adapter.mercadolivre.mlenum.MLStatusEnum;
import org.florense.outbound.adapter.postgre.mappers.VendaEntityMapper;
import org.florense.outbound.adapter.postgre.repository.VendaRepository;
import org.florense.outbound.port.postgre.VendaEntityPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class VendaAdapter implements VendaEntityPort {

    @Inject
    VendaRepository vendaRepository;
    @Inject
    VendaEntityMapper vendaEntityMapper;

    @Override
    public Pagination<Venda> listByAnuncio(Anuncio anuncio, VendaFilter filter, PageParam pageParam){
        Pageable pageable = PageRequest.of(pageParam.getPage(), pageParam.getPageSize());
        Sort.Direction sortDirection = pageParam.getSortType().equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if(Objects.equals(pageParam.getSortField(), "id")) pageParam.setSortField(String.format("v.%s",pageParam.getSortField()));
        String status = filter.isIncludeCancelled() ?
                String.format("%s and %s", MLStatusEnum.PAID.getIdentifier(), MLStatusEnum.CANCELLED.getIdentifier()) : MLStatusEnum.PAID.getIdentifier();

        var page = vendaRepository.listByFilters(filter.getOrderCreationInicial(), filter.getOrderCreationFinal(), anuncio.getId(), status,
                Sort.by(sortDirection,pageParam.getSortField()), pageable);

        return new Pagination<>(pageParam.getPage(), pageParam.getPageSize(), page.getTotalPages(), (int) page.getTotalElements(), pageParam.getSortField(),
                pageParam.getSortType(), page.stream().map(vendaEntityMapper::toModel).collect(Collectors.toList()));
    }
}
