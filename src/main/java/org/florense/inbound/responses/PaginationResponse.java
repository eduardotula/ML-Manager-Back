package org.florense.inbound.responses;

import lombok.*;
import org.florense.domain.model.Pagination;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    private MetaInfo metaInfo;
    private List<T> results;

    public PaginationResponse(Pagination<T> pagination, Object search) {
        this.metaInfo = new MetaInfo();
        this.metaInfo.setEmpty(pagination.getResults().isEmpty());
        this.metaInfo.getPageInfo().setFirst(pagination.getPage().equals(0));
        this.metaInfo.getPageInfo().setLast(pagination.getPage().equals(pagination.getTotalPages()-1));
        this.metaInfo.getPageInfo().setPage(pagination.getPage());
        this.metaInfo.getPageInfo().setPageSize(pagination.getPageSize());
        this.metaInfo.getPageInfo().setTotalPages(pagination.getTotalPages());
        this.metaInfo.setTotalElements(pagination.getTotalElements());
        this.metaInfo.getSortInfo().setSortField(pagination.getSortField());
        this.metaInfo.getSortInfo().setSortType(pagination.getSortType());
        this.results = pagination.getResults();
    }
}