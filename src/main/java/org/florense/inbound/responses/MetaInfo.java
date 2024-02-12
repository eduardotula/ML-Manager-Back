package org.florense.inbound.responses;

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
public class MetaInfo {
    private boolean empty;
    private PageInfo pageInfo = new PageInfo();
    private SortInfo sortInfo = new SortInfo();
    private Map<String, Object> search;
    private Integer totalElements;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageInfo {
        private boolean first;
        private boolean last;
        private Integer page;
        private Integer pageSize;
        private Integer totalPages;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortInfo {
        private String sortField;
        private String sortType;
        private List<String> avaliableSortingFields;
    }
}