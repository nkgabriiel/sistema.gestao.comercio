package sistema.gestao.model;

public class Fornecedor {
    private Long id;
    private String cnpj;
    private String nomeFantasia;
    private String nomeRepresentante;
    private String contatoRepresentante;

    public Fornecedor(String cnpj, String nomeFantasia, String nomeRepresentante, String contatoRepresentante) {
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.nomeRepresentante = nomeRepresentante;
        this.contatoRepresentante = contatoRepresentante;
    }

    public Fornecedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getNomeRepresentante() {
        return nomeRepresentante;
    }

    public void setNomeRepresentante(String nomeRepresentante) {
        this.nomeRepresentante = nomeRepresentante;
    }

    public String getContatoRepresentante() {
        return contatoRepresentante;
    }

    public void setContatoRepresentante(String contatoRepresentante) {
        this.contatoRepresentante = contatoRepresentante;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", cnpj='" + cnpj + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", nomeRepresentante='" + nomeRepresentante + '\'' +
                ", contatoRepresentante='" + contatoRepresentante + '\'' +
                '}';
    }
}
