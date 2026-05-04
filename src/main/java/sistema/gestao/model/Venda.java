package sistema.gestao.model;

import java.time.LocalDateTime;
import java.util.List;

public class Venda {
    private Long id;
    private Funcionario vendedor;
    private Cliente comprador;
    private List<ItemVenda> itens;
    private LocalDateTime dataHora;
    private FormaPagamento formaPagamento;
    private Double valorTotal;
    private Double valorCobrado;

    public Venda(Funcionario vendedor, Cliente comprador, List<ItemVenda> itens, LocalDateTime dataHora,
                 FormaPagamento formaPagamento, Double valorTotal,
                 Double valorCobrado) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.itens = itens;
        this.dataHora = dataHora;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
        this.valorCobrado = valorCobrado;
    }


    public Venda() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Funcionario vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(Double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public void aplicarMetodoPagamento(FormaPagamento metodoEscolhido) {
        this.valorCobrado = metodoEscolhido.getForma().calcularValorFinal(this.valorTotal);
    }
}
