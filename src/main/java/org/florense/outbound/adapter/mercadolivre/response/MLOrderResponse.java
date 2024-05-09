package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.florense.outbound.adapter.mercadolivre.exceptions.MLErrorTypesEnum;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreClientException;
import org.florense.outbound.adapter.mercadolivre.exceptions.MercadoLivreException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MLOrderResponse {

    @JsonProperty("id")
    private Long orderId;

    @JsonIgnore
    private Long shippingId;

    @JsonProperty("date_closed")
    private ZonedDateTime orderCreationTime;
    private String status;
    @JsonProperty("total_amount")
    private double precoDesconto;
    @JsonProperty("fulfilled")
    private Boolean completo;
    @JsonIgnore
    private List<Long> mediations = new ArrayList<>();
    @JsonIgnore
    private double saleFee;

    @JsonIgnore
    private String mlId;
    @JsonIgnore
    private String title;
    @JsonIgnore
    private int quantity = 0;

    @JsonProperty("order_items")
    private void setOrderItems(List<Map<String, Object>> list) throws MercadoLivreException {
        if(!list.isEmpty()){
            var orderItems = list.get(0);
            if(orderItems != null){

                var quantity = orderItems.get("quantity");
                if(Objects.nonNull(quantity)) this.quantity = ((Number) quantity).intValue();
                else throw new MercadoLivreException("Falha ao mapear resposta de MLClient quantity nao encontrado em order_items", "setOrderItems", MLErrorTypesEnum.DEFAULT, null);

                var saleFee = orderItems.get("sale_fee");
                if(Objects.nonNull(saleFee)) this.saleFee = ((Number) saleFee).doubleValue();
                else throw new MercadoLivreException("Falha ao mapear resposta de MLClient sale_fee nao encontrado em order_items", "setOrderItems", MLErrorTypesEnum.DEFAULT, null);

                var item = (Map<String, Object>)list.get(0).get("item");
                if(item != null){
                    var mlId = item.get("id");
                    if(Objects.nonNull(mlId)) this.mlId = (String) mlId;
                    else throw new MercadoLivreException("Falha ao mapear resposta de MLClient id nao encontrado em order_items", "setOrderItems", MLErrorTypesEnum.DEFAULT, null);

                    var title = item.get("title");
                    if(Objects.nonNull(title)) this.title = (String) title;
                    else throw new MercadoLivreException("Falha ao mapear resposta de MLClient title nao encontrado em order_items", "setOrderItems", MLErrorTypesEnum.DEFAULT, null);

                }else throw new MercadoLivreException("Falha ao mapear resposta de MLClient item nao encontrado em order_items", "setOrderItems", MLErrorTypesEnum.DEFAULT, null);

            }
        }
    }

    @JsonProperty("mediations")
    private void setMediations(List<Map<String, Long>> list){
        if(Objects.isNull(list) || list.isEmpty()) return;
        this.mediations = new ArrayList<>(list.get(0).values());
    }

    @JsonProperty("shipping")
    private void setShipping(Map<String, Long> list){
        this.shippingId = list.get("id") != null ? list.get("id") : 0L;
    }
}
