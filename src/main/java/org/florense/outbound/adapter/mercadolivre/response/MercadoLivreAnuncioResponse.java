package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MercadoLivreAnuncioResponse {

    @JsonProperty("id")
    private String mlId;
    private String title;
    private String permalink;
    private String category_id;
    private double price;
    @JsonIgnore
    private String gtin;
    @JsonIgnore
    private String sku;
    private String status;
    private String listing_type_id;

    @JsonSetter("attributes")
    private void setTheAttributes(List<Map<String, Object>> list){
        this.sku = "";
        this.gtin = "";
        list.forEach(at ->{
            if(at.get("id").equals("SELLER_SKU")) this.sku = (String) at.get("value_name");
            else if(at.get("id").equals("GTIN")) this.gtin = (String)at.get("value_name");
        });
    }

}




