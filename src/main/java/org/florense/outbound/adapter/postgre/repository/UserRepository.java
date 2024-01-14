package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
