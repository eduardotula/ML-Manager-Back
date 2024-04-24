package org.florense.outbound.adapter.postgre.entity;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "reclamacao")
@Table(name = "reclamacao")
public class ReclamacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "reclamacao_id")
    private long reclamacaoId;

    @NotNull
    @Column(name = "resource_id")
    private long resourceId;

    @NotNull
    @Column(name = "type", columnDefinition = "VARCHAR(50)")
    private String type;

    @NotNull
    @Column(name = "stage", columnDefinition = "VARCHAR(100)")
    private String stage;

    @NotNull
    @Column(name = "status", columnDefinition = "VARCHAR(50)")
    private String status;

    @NotNull
    @Column(name = "reason", columnDefinition = "VARCHAR(50)")
    private String reason;

    @NotNull
    @Column(name = "closed_by", columnDefinition = "VARCHAR(200)")
    private String closedBy;

    @NotNull
    @Column(name = "benefited", columnDefinition = "VARCHAR(200)")
    private String benefited;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "reclamacao_created_at")
    private LocalDateTime reclamacaoCreatedAt;

    @ManyToOne
    @JoinColumn(name = "order_id_fk")
    private OrderEntity order;
}
