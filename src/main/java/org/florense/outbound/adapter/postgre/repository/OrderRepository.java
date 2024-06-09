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

    OrderEntity findFirstByOrderByIdDesc();

    @Query(value = "from orderM o " +
            " join o.vendas v " +
            " join v.anuncio a " +
            "WHERE (cast(:dataInicial as timestamp) IS NULL OR (o.orderCreationTime BETWEEN :dataInicial AND :dataFinal)) " +
            " AND (:descricao IS NULL OR a.descricao LIKE cast('%'||cast(:descricao as text)||'%' as text)) " +
            " AND v.anuncio.user.id = :userId ")
    Page<OrderEntity> listByFilters(@Param("dataInicial") LocalDateTime dataInicial,
                                    @Param("dataFinal") LocalDateTime dataFinal,
                                    @Param("userId") Long userId,
                                    @Param("descricao") String descricao,
                                    Sort sort,
                                    Pageable pageable);
    @Query(value = "from orderM o " +
            " join o.vendas v " +
            " join v.anuncio a " +
            " where a.id = :anuncioId")
    List<OrderEntity> listOrdersByAnuncio(@Param("anuncioId") Long anuncioId);

    @Query(value = "from orderM o " +
            " join o.vendas v " +
            " join v.anuncio a " +
            " join a.user u " +
            " where u.id = :userId")
    List<OrderEntity> listOrdersByUserId(@Param("userId") Long userId);
}
