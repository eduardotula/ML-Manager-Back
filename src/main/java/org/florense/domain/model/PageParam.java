package org.florense.domain.model;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    private int page;
    private int pageSize;
    private String sortField;
    private String sortType;
}