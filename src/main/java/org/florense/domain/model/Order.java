package org.florense.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long id;
    private Long orderId;
    private Long packId;
    private List<Venda> vendas;
    private boolean completo;
    private String status;
    private LocalDateTime orderCreationTime;
    private LocalDateTime createdAt;
}
