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
    private List<Reclamacao> reclamacaoes;
    private LocalDateTime orderCreationTime;
    private LocalDateTime createdAt;

    public void updateVendasByMatchingByMlId(List<Venda> oldVendas){
        oldVendas.forEach(oldVenda ->{
            var incomplete = this.vendas.stream().filter(newVenda -> Objects.equals(newVenda.getAnuncio().getMlId(),
                    oldVenda.getAnuncio().getMlId())).findFirst();

            incomplete.ifPresent(venda -> {
                venda.setId(oldVenda.getId());
                venda.setCreatedAt(oldVenda.getCreatedAt());
            });
        });
    }
}
