package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {


    List<AnuncioEntity> findByMlIdAndUserId(String mlId, Long userId);

    @Query("select a from anuncio a where a.user.id = ?1 order by a.id asc")
    List<AnuncioEntity> findByOrderByIdAsc(Long userId);

    @Query("select a from anuncio a where a.user.id = ?1 and a.registered order by a.id asc")
    List<AnuncioEntity> findRegisteredByOrderByIdAsc(Long userId);

}
