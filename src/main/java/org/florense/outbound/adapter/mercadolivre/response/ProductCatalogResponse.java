package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCatalogResponse {

    @JsonProperty("item_id")
    private String mlId;
    private double price;
    @JsonProperty("seller_id")
    private String sellerId;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("listing_type_id")
    private String listingTypeId;
    @JsonIgnore
    private boolean freeShipping;
    @JsonIgnore
    private String logisticType;

    @JsonProperty("shipping")
    private void setShipping(Map<String, String> list){
        if(list.isEmpty()) return;

        this.freeShipping = list.get("free_shipping") != null && Boolean.parseBoolean(list.get("free_shipping"));
        this.logisticType = list.get("cross_docking") != null ? list.get("cross_docking") : "";
    }

}
