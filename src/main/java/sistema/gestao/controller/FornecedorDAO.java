package sistema.gestao.controller;

import sistema.gestao.model.Fornecedor;

import java.sql.*;

public class FornecedorDAO {
    public void criarFornecedor(Fornecedor fornecedor) {
        String sql = """
                INSERT IGNORE INTO fornecedor (cnpj, nome_fantasia, nome_representante, contato_representante) 
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNomeFantasia());
            stmt.setString(3, fornecedor.getNomeRepresentante());
            stmt.setString(4, fornecedor.getContatoRepresentante());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fornecedor.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar fornecedor." + e.getMessage());
        }
    }

    public Fornecedor buscarFornecedorPorId(Long id) {
        String sql = """
                SELECT * FROM fornecedor WHERE id = ?;
                """;

        Fornecedor fornecedorEncontrado = null;
        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    fornecedorEncontrado = new Fornecedor();

                    fornecedorEncontrado.setId(rs.getLong("id"));
                    fornecedorEncontrado.setCnpj(rs.getString("cnpj"));
                    fornecedorEncontrado.setNomeFantasia(rs.getString("nome_fantasia"));
                    fornecedorEncontrado.setNomeRepresentante(rs.getString("nome_representante"));
                    fornecedorEncontrado.setContatoRepresentante(rs.getString("contato_representante"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar fornecedor." + e.getMessage());
        }
        return fornecedorEncontrado;
    }
}
