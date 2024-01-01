package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.florense.outbound.adapter.postgre.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(Long orderId);

    Optional<OrderEntity> findTopByOrderByIdDesc();

    @Query(value = "from order o WHERE " + "(o.orderCreationTime BETWEEN :dataInicial AND :dataFinal)")
    Page<OrderEntity> listByFilters(@Param("dataInicial") LocalDateTime dataInicial,
                                    @Param("dataFinal") LocalDateTime dataFinal,
                                    Sort sort,
                                    Pageable pageable);
}
