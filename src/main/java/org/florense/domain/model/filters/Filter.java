package org.florense.domain.model.filters;

import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Filter {

    static Map<String, String> getAvaliableSortTypes() {
        return new HashMap<>();
    }

    Sort getSort();
}
