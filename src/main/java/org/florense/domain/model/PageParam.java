package org.florense.domain.model;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    private Integer page;
    private Integer pageSize;
    private String sortField;
    private String sortType;
}