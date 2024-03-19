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

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VendaAdapter implements VendaEntityPort {

    @Inject
    VendaRepository vendaRepository;
    @Inject
    VendaEntityMapper vendaEntityMapper;

    @Override
    public Pagination<Venda> listByAnuncio(Anuncio anuncio, VendaFilter filter){
        Pageable pageable = PageRequest.of(filter.getPageParam().getPage(), filter.getPageParam().getPageSize());

        List<String> status = new ArrayList<>(List.of(new String[]{MLStatusEnum.PAID.getIdentifier()    }));
        if(filter.isIncludeCancelled()){
            status.add(MLStatusEnum.CANCELLED.getIdentifier());
        }

        var page = vendaRepository.listByFilters(filter.getOrderCreationInicial(), filter.getOrderCreationFinal(), anuncio.getId(), status,
                filter.getSort(), pageable);

        return new Pagination<>(filter.getPageParam().getPage(), filter.getPageParam().getPageSize(), page.getTotalPages(), (int) page.getTotalElements(), filter.getPageParam().getSortField(),
                filter.getPageParam().getSortType().name(), filter.getPageParam().getAvaliableSortTypes(),page.stream().map(vendaEntityMapper::toModel).collect(Collectors.toList()));
    }
}
