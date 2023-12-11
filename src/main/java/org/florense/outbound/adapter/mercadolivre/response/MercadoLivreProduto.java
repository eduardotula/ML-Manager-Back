package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonDeserialize(using = GtinDese.class)
    private String gtin;
    @JsonDeserialize(using = SkuDese.class)
    private String sku;
    private String status;



}

class GtinDese extends JsonDeserializer<String> {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        var nodes = mapper.readTree(p.getText());
        for (int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).get("id").asText().equals("SKU")) return nodes.get(i).get("value_name").asText();

        }
        return "";
    }
}

class SkuDese extends JsonDeserializer<String> {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        var nodes = mapper.readTree(p.getText());
        for (int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).get("id").asText().equals("GTIN")) return nodes.get(i).get("value_name").asText();

        }
        return "";
    }
}



