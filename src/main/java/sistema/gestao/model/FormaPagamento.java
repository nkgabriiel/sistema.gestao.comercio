package sistema.gestao.model;

public enum FormaPagamento {
    DINHEIRO(valor -> valor * 0.95),
    PIX(valor -> valor * 0.95),
    DEBITO(valor -> valor),
    CREDITO(valor -> valor);

    private final CalculoPagamento forma;

    FormaPagamento(CalculoPagamento forma) {
        this.forma = forma;
    }

    public CalculoPagamento getForma() {
        return forma;
    }
}
