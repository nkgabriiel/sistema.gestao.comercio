package sistema.gestao.model;

public class Cliente extends Pessoa {
    private Long id;
    private Integer pontoFidelidade;

    public Cliente(String nome, String cpf, String email, Integer pontoFidelidade) {
        super(nome, cpf, email);
        this.pontoFidelidade = pontoFidelidade;
    }

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPontoFidelidade() {
        return pontoFidelidade;
    }

    public void setPontoFidelidade(Integer pontoFidelidade) {
        this.pontoFidelidade = pontoFidelidade;
    }
}
