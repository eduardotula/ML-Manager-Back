package org.florense.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long id;
    private Long orderId;
    private Long shippingId;
    private List<Venda> vendas;
    private LocalDateTime orderCreationTime;
    private LocalDateTime createdAt;

    public void updateVendasByMatchingByMlId(List<Venda> newVendas){
        newVendas.forEach(newVenda ->{
            var incomplete = this.vendas.stream().filter(old -> Objects.equals(old.getAnuncio().getMlId(),
                    newVenda.getAnuncio().getMlId())).findFirst();

            incomplete.ifPresent(venda -> newVenda.setId(venda.getOrderId()));
        });
    }
}
