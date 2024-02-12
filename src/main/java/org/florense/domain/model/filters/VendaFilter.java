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
@AllArgsConstructor
@NoArgsConstructor
public class VendaFilter implements Filter {
    private LocalDateTime orderCreationInicial = null;
    private LocalDateTime orderCreationFinal = null;
    private boolean includeCancelled = false;
    private PageParam pageParam;

    public static Map<String, String> getAvaliableSortTypes() {
        var map = new HashMap<String, String>();
        map.put("id","venda.id");
        return map;
    }

    public Sort getSort(){
        if(VendaFilter.getAvaliableSortTypes().containsKey(this.pageParam.getSortField())){
            var a = Sort.by(this.pageParam.getSortType(),VendaFilter.getAvaliableSortTypes().get(this.pageParam.getSortField()));
            return a;
        }
        return Sort.by(Sort.Direction.ASC,"id");
    }
}
