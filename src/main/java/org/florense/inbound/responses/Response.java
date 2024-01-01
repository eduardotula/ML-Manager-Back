package org.florense.inbound.responses;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private MetaInfo metaInfo;
    private T results;

    public Response(T results) {
        this.metaInfo = new MetaInfo();
        this.metaInfo.setEmpty(false);
        this.metaInfo.setTotalElements(1);
        this.results = results;
    }
}