package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "orderM")
@Table(name = "orderM")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "shipping_id")
    private Long shippingId;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaEntity> vendas;

    @Column(name = "order_creation_time")
    private LocalDateTime orderCreationTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
