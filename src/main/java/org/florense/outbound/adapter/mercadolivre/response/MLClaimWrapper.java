package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MLClaimWrapper {

    @JsonIgnore
    int total;
    @JsonIgnore
    int offset;
    @JsonIgnore
    int limit;

    public List<MLClaimResponse> data;

    @JsonProperty("paging")
    private void setPaging(Map<String, Integer> paging){
        this.total = paging.getOrDefault("total", 0);
        this.offset = paging.getOrDefault("offset", 0);
        this.limit = paging.getOrDefault("limit", 0);
    }
}
