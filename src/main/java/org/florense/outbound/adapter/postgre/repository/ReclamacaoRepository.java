package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.ReclamacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamacaoRepository extends JpaRepository<ReclamacaoEntity, Long> {
}
