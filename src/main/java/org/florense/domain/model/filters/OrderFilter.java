package org.florense.domain.model.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.florense.domain.model.PageParam;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilter implements Filter{

    private LocalDateTime orderCreationInicial;
    private LocalDateTime orderCreationFinal;
    private String descricao;
    private PageParam pageParam;


    public static Map<String, String> getAvaliableSortTypes() {
        var map = new HashMap<String, String>();
        map.put("id","order.id");
        return map;
    }

    public Sort getSort(){
        if(OrderFilter.getAvaliableSortTypes().containsKey(this.pageParam.getSortField())){
            return Sort.by(this.pageParam.getSortType(),OrderFilter.getAvaliableSortTypes().get(this.pageParam.getSortField()));
        }
        return Sort.by(Sort.Direction.ASC,"id");
    }
}
