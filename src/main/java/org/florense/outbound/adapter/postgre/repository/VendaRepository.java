package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<VendaEntity, Long> {

    @Query(value = "from venda venda " +
            " join venda.order o" +
            " join venda.anuncio a" +
            " WHERE (cast(:dataInicial as timestamp) IS NULL OR (o.orderCreationTime BETWEEN :dataInicial AND :dataFinal)) " +
            " and a.id = :anuncioId" +
            " and (venda.status in (:status))")
    Page<VendaEntity> listByFilters(@Param("dataInicial") LocalDateTime dataInicial,
                                      @Param("dataFinal") LocalDateTime dataFinal,
                                      @Param("anuncioId") Long anuncioId,
                                      @Param("status") List<String> status,
                                      Sort sort,
                                      Pageable pageable);
}
