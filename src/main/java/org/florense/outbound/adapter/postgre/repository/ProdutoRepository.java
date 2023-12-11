package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {
}
