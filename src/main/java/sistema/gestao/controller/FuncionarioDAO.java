package sistema.gestao.controller;

import sistema.gestao.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    public void criarFuncionario(Funcionario funcionario) {
        String sql = """
                INSERT IGNORE INTO funcionario (nome, cpf, email, matricula, cargo, percentual_comissao)
                VALUES (?, ?, ?, ?, ?, ?);
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, funcionario.getMatricula());
            stmt.setString(5, funcionario.getCargo());
            stmt.setDouble(6, funcionario.getPercentualComissao());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    funcionario.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar funcionário" + e.getMessage());
        }
    }

    public Funcionario encontrarFuncionarioPorId(Long id) {
        String sql = """
                SELECT * FROM funcionario WHERE id = ?;
                """;

        Funcionario funcionarioEncontrado = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionarioEncontrado = new Funcionario();

                    funcionarioEncontrado.setId(rs.getLong("id"));
                    funcionarioEncontrado.setNome(rs.getString("nome"));
                    funcionarioEncontrado.setCpf(rs.getString("cpf"));
                    funcionarioEncontrado.setEmail(rs.getString("email"));
                    funcionarioEncontrado.setCargo(rs.getString("cargo"));
                    funcionarioEncontrado.setPercentualComissao(rs.getDouble("percentual_comissao"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao procurar funcionário." + e.getMessage());
        }
        return funcionarioEncontrado;
    }

    public List<Funcionario> listarFuncionarios() {
        List<Funcionario> listaFuncionarios = new ArrayList<>();

        String sql = """
                SELECT * FROM funcionarios;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("matricula"),
                        rs.getString("cargo"),
                        rs.getDouble("percentual_comissao")
                );
                funcionario.setId(rs.getLong("id"));
                listaFuncionarios.add(funcionario);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários" + e.getMessage());
        }
        return listaFuncionarios;
    }

    public boolean atualizarFuncionario(Funcionario funcionario) {
        String sql = """
                UPDATE funcionario SET nome = ?, cpf = ?, email = ?, matricula = ?, cargo = ?, percentual_comissao = ?
                WHERE  id = ?;
                """;

        boolean funcionarioAtualizado = false;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, funcionario.getMatricula());
            stmt.setString(5, funcionario.getCargo());
            stmt.setDouble(6, funcionario.getPercentualComissao());
            stmt.setLong(7, funcionario.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if(linhasAfetadas > 0) {
                funcionarioAtualizado = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário" + e.getMessage());
        }
        return funcionarioAtualizado;
    }

    public void deletarFuncionario(Long id) {
        String sql = """
                DELETE FROM funcionario WHERE id = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Funcionario deletado com sucesso.");

        } catch (SQLException e) {
           throw new RuntimeException("Erro ao deletar funcionario. " + e.getMessage());
        }
    }
}
