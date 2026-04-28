package sistema.gestao.controller;

import sistema.gestao.model.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Fornecedor> listarFornecedores() {
        List<Fornecedor> listaFornecedores = new ArrayList<>();

        String sql = """
                SELECT * FROM fornecedor;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                Fornecedor fornecedor = new Fornecedor(
                        rs.getString("cnpj"),
                        rs.getString("nome_fantasia"),
                        rs.getString("nome_representante"),
                        rs.getString("contato_representante")
                );
                fornecedor.setId(rs.getLong("id"));
                listaFornecedores.add(fornecedor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fornecedores." + e.getMessage());
        }
        return listaFornecedores;
    }

    public boolean atualizarFornecedor(Fornecedor fornecedor) {
        String sql = """
                UPDATE fornecedor SET nome_representante = ?, contato_representante = ?, nome_fantasia = ?, cnpj = ?
                WHERE id = ?;
                """;

        boolean fornecedorAtualizado = false;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNomeRepresentante());
            stmt.setString(2, fornecedor.getContatoRepresentante());
            stmt.setString(3, fornecedor.getNomeFantasia());
            stmt.setString(4, fornecedor.getCnpj());
            stmt.setLong(5, fornecedor.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if(linhasAfetadas > 0) {
                fornecedorAtualizado = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fornecedor." + e.getMessage());
        }
        return fornecedorAtualizado;
    }

    public void deletarFornecedor (Long id) {
        String sql = """
                DELETE FROM fornecedor WHERE id = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Fornecedor deletado com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar fornecedor." + e.getMessage());
        }

    }

}
