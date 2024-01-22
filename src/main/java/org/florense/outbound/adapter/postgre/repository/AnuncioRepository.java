package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long> {


    List<AnuncioEntity> findByMlId(String mlId);
    List<AnuncioEntity> findWhereRegisteredByOrderByIdAsc();

}
