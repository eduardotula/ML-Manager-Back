package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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
    private String mlId;
    @JsonIgnore
    private String title;
    @JsonIgnore
    private Integer quantity;

    @JsonProperty("order_items")
    private void setOrderItems(List<Map<String, Object>> list){
        if(list.isEmpty()){
            this.quantity = 0;
            this.mlId = "";
            this.title = "";
            this.shippingId = 0L;
        }else{
            var orderItems = list.get(0);
            this.quantity = (Integer) orderItems.get("quantity");

            var item = (Map<String, Object>)list.get(0).get("item");
            this.mlId = (String) item.get("id");
            this.title = (String) item.get("title");

            var shipping = (Map<String, Object>)list.get(0).get("shipping");
            this.shippingId = (Long)shipping.get("id");
        }
    }
}
