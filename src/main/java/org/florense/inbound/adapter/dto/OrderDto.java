package org.florense.inbound.adapter.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Long orderId;
    private Long packId;
    private List<VendaDto> vendas;
    private boolean completo;
    private String status;
    private LocalDateTime orderCreationTime;
    private LocalDateTime createdAt;
}
