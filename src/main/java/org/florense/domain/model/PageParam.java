package org.florense.domain.model;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class PageParam {
    private int page = 0;
    private int pageSize = 0;
    private String sortField = null;
    private Sort.Direction sortType = null;
    @Getter(AccessLevel.NONE)
    private Map<String, String> avaliableSortTypes;

    public PageParam(int page, int pageSize, String sortField, String sortType, Map<String, String> avaliableSortTypes) {
        this.page = page;
        this.pageSize = pageSize;
        this.avaliableSortTypes = avaliableSortTypes;
        this.sortField = sortField;
        this.sortType = sortType.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public List<String> getAvaliableSortTypes(){
        return new ArrayList<>(this.avaliableSortTypes.keySet());
    }
}