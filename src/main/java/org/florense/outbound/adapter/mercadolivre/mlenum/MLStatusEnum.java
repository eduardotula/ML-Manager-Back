package org.florense.outbound.adapter.mercadolivre.mlenum;

public enum MLStatusEnum {

    CONFIRMED("Status inicial de uma order; ainda sem ter sido paga.", "confirmed"),
    PAYMENT_REQUIRED("O pagamento da order deve ter sido confirmado para exibir as informações do usuário.", "payment_required"),
    PAYMENT_IN_PROCESS("Há um pagamento relacionado à order, mais ainda não foi aprovado.", "payment_in_process"),
    PARTIALLY_PAID("A order tem um pagamento associado creditado, porém, insuficiente.", "partially_paid"),
    PAID("A order tem um pagamento associado aprovado.", "paid"),
    PARTIALLY_REFUNDED("A order tem devoluções paciais de seus pagamentos.", "partially_refunded"),
    PENDING_CANCEL("Quando a order foi cancelada mas temos dificuldade para devolver o pagamento.", "pending_cancel"),
    CANCELLED("Por alguma razão, a order não foi completada.", "cancelled"),
    INVALID("A order foi invalidada por vir de um comprador malicioso.", "invalid");

    private final String description;
    private final String identifier;

    MLStatusEnum(String description, String identifier) {
        this.description = description;
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentifier() {
        return identifier;
    }
}
