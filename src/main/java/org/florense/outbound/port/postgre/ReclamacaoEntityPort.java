package org.florense.outbound.port.postgre;

import org.florense.domain.model.Reclamacao;

public interface ReclamacaoEntityPort {


    Reclamacao createUpdate(Reclamacao reclamacao);

    Reclamacao findByReclamacaoId(Long reclamacaoId);
}
