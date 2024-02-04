package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface VendaRepository extends JpaRepository<VendaEntity, Long> {

    @Query(value = "from venda v " +
            " join v.order o" +
            " join v.anuncio a" +
            " WHERE (cast(:dataInicial as timestamp) IS NULL OR (o.orderCreationTime BETWEEN :dataInicial AND :dataFinal)) " +
            " and v.status = :status " +
            " and a.id = :anuncioId")
    Page<VendaEntity> listByFilters(@Param("dataInicial") LocalDateTime dataInicial,
                                      @Param("dataFinal") LocalDateTime dataFinal,
                                      @Param("anuncioId") Long anuncioId,
                                      @Param("status") String status,
                                      Sort sort,
                                      Pageable pageable);
}
