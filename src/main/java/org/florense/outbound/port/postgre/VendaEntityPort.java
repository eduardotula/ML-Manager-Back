package org.florense.outbound.port.postgre;

import org.florense.domain.model.Anuncio;
import org.florense.domain.model.PageParam;
import org.florense.domain.model.Pagination;
import org.florense.domain.model.Venda;
import org.florense.domain.model.filters.VendaFilter;

public interface VendaEntityPort {
    Pagination<Venda> listByAnuncio(Anuncio anuncio, VendaFilter filter, PageParam pageParam);
}
