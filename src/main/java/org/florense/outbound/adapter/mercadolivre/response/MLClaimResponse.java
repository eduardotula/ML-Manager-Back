package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MLClaimResponse {

    @JsonProperty("id")
    public Long reclamacaoId;

    @JsonProperty("resource_id")
    public Long resourceId;
    public String type = "";
    public String stage = "";

    public String status = "";
    @JsonIgnore
    public String reason = "";
    @JsonIgnore
    public String closedBy = "";
    @JsonIgnore
    private String benefited = "";

    @JsonIgnore
    private LocalDateTime reclamacaoCreatedAt;

    @JsonProperty("date_created")
    public void setDateCreated(ZonedDateTime dateCreated){
        this.reclamacaoCreatedAt = dateCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @JsonProperty("resolution")
    public void setResolutions(Map<String, Object> resolution){
        if(Objects.nonNull(resolution) && !resolution.isEmpty()){
            Object reasonObj = resolution.get("reason");
            if(Objects.nonNull(reasonObj)) this.reason = (String) reasonObj;

            Object closedByObj = resolution.get("closed_by");
            if(Objects.nonNull(closedByObj)) this.closedBy = (String) closedByObj;

            Object benefitedObj = resolution.get("benefited");
            if(Objects.nonNull(benefitedObj)){
                ((List<String>) benefitedObj).forEach(benefited ->
                        this.benefited = "".concat(benefited + ","));
            }
        }
    }

}
