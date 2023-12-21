package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MercadoLivreProduto {

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




