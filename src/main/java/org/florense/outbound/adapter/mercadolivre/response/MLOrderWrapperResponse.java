package org.florense.outbound.adapter.mercadolivre.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MLOrderWrapperResponse {

    @JsonProperty("results")
    List<MLOrderResponse> orderResponses = new ArrayList<>();

    @JsonIgnore
    int total;
    @JsonIgnore
    int offset;
    @JsonIgnore
    int limit;


    @JsonProperty("paging")
    private void setPaging(Map<String, Integer> paging){
        this.total = paging.getOrDefault("total", 0);
        this.offset = paging.getOrDefault("offset", 0);
        this.limit = paging.getOrDefault("limit", 0);
    }

}
