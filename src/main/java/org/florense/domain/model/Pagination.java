package org.florense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {
    private  Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalElements;
    private String sortField;
    private String sortType;
    private List<T> results;

    public <F> Pagination<F> to(Function<T, F> converter){
        Pagination pagination = new Pagination<F>();
        pagination.setResults(new ArrayList());
        pagination.setPage(this.page);
        pagination.setPageSize(this.pageSize);
        pagination.setTotalElements(this.totalElements);
        pagination.setSortField(this.sortField);
        pagination.setSortType(this.sortType);
        pagination.setTotalPages(this.totalPages);
        try{
            this.results.forEach(t -> pagination.getResults().add(converter.apply(t)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return pagination;
    }
}