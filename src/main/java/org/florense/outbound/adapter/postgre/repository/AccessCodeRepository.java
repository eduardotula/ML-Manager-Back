package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.AccessCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccessCodeRepository extends JpaRepository<AccessCodeEntity, Long> {

}
