package org.florense.outbound.adapter.postgre.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.outbound.adapter.postgre.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@ApplicationScoped
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
