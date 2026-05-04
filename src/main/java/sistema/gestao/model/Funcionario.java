package sistema.gestao.model;

public class Funcionario extends Pessoa {
    private Long id;
    private String matricula;
    private String cargo;
    private double percentualComissao;

    public Funcionario(String nome, String cpf, String email, String matricula, String cargo, double percentualComissao) {
        super(nome, cpf, email);
        this.matricula = matricula;
        this.cargo = cargo;
        this.percentualComissao = percentualComissao;
    }

    public Funcionario(String nome, String cpf, String email, String matricula) {
        super(nome, cpf, email);
        this.matricula = matricula;
    }

    public Funcionario(String nome, String cpf, String email) {
        super(nome, cpf, email);
    }

    public Funcionario() {
        super();
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getPercentualComissao() {
        return percentualComissao;
    }

    public void setPercentualComissao(double percentualComissao) {
        this.percentualComissao = percentualComissao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                ", cargo='" + cargo + '\'' +
                ", percentualComissao=" + percentualComissao +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
