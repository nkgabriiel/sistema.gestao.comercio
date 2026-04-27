package sistema.gestao.model;

public class Cliente extends Pessoa {
    private Long id;
    private Integer pontosDeFidelidade;

    public Cliente(String nome, String cpf, String email, Integer pontosDeFidelidade) {
        super(nome, cpf, email);
        this.pontosDeFidelidade = pontosDeFidelidade;
    }

    public Cliente() {
        super();
    }

    public Integer getPontosDeFidelidade() {
        return pontosDeFidelidade;
    }

    public void setPontosDeFidelidade(Integer pontosDeFidelidade) {
        this.pontosDeFidelidade = pontosDeFidelidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
