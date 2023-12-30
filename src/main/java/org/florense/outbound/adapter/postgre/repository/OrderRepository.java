package org.florense.outbound.adapter.postgre.repository;

import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.florense.outbound.adapter.postgre.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(Long orderId);
}
