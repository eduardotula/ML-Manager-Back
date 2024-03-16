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
    private Long shippingId;
    private List<VendaDto> vendas;
    private LocalDateTime orderCreationTime;
    private LocalDateTime createdAt;
}
