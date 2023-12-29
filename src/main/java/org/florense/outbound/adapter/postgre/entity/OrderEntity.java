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
@Entity(name = "order")
@Table(name = "orderM")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "pack_id")
    private Long packId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaEntity> vendas;
    @Column(name = "completo")
    private boolean completo;
    @Column(name = "stauts")
    private String status;

    @Column(name = "order_creation_time")
    private LocalDateTime orderCreationTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
