package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int quantity;

    @JsonProperty("order_items")
    private void setOrderItems(List<Map<String, Object>> list){
        if(list.isEmpty()){
            this.quantity = 0;
            this.mlId = "";
            this.title = "";
        }else{
            var orderItems = list.get(0);
            this.quantity = ((Number) orderItems.get("quantity")).intValue();
            this.saleFee = ((Number) orderItems.get("sale_fee")).doubleValue();

            var item = (Map<String, Object>)list.get(0).get("item");
            this.mlId = (String) item.get("id");
            this.title = (String) item.get("title");
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
