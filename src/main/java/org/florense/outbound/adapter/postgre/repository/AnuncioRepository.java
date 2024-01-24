package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {


    @Query("select a from anuncio a where a.mlId = ?1 and a.user.id = ?2 and a.complete")
    List<AnuncioEntity> findByMlIdAndUserId(String mlId, Long userId);

    @Query("select a from anuncio a where a.user.id = ?1 order by a.id asc")
    List<AnuncioEntity> findByOrderByIdAsc(Long userId);

    @Query("select a from anuncio a where a.user.id = ?1 and a.complete order by a.id asc")
    List<AnuncioEntity> findRegisteredByOrderByIdAsc(Long userId);

    @Query("select a from anuncio a where a.user.id = ?2 and a.mlId = ?1")
    Optional<AnuncioEntity> findAnyByMlId(String mlId, Long userId);

    @Query("select a from anuncio a where a.id = ?1 and a.complete")
    Optional<AnuncioEntity> find(Long id);

    @Query("select a from anuncio a where a.id = ?1")
    Optional<AnuncioEntity> findAnyById(Long id);

}
