package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {


    List<ProdutoEntity> findByMlId(String mlId);
}
