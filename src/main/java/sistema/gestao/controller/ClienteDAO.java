package sistema.gestao.controller;

import sistema.gestao.model.Cliente;

import java.lang.foreign.SequenceLayout;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void inserirCliente(Cliente cliente) {
        String sql = """
                INSERT IGNORE INTO cliente (nome, cpf, email, ponto_fidelidade)
                VALUES (?, ?, ?, ?);
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setInt(4, cliente.getPontosDeFidelidade());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys();) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }
            System.out.println("Cliente salvo com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente." + e.getMessage());
        }
    }

    public boolean atualizarCliente(Cliente cliente) {
        String sql = """
                UPDATE cliente SET nome = ?, cpf = ?, email = ?, ponto_fidelidade = ? WHERE id = ?;
                """;

        boolean usuarioAtualizado = false;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setInt(4, cliente.getPontosDeFidelidade());
            stmt.setLong(5, cliente.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                usuarioAtualizado = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tentar atualizar usuário;" + e.getMessage());
        }
        return usuarioAtualizado;
    }

    public Cliente acharClientePorId (Long id) {
        String sql = """
                SELECT * FROM cliente WHERE id = ?;
                """;

        Cliente clienteEncontrado = null;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    clienteEncontrado = new Cliente();

                    clienteEncontrado.setId(rs.getLong("id"));
                    clienteEncontrado.setNome(rs.getString("nome"));
                    clienteEncontrado.setCpf(rs.getString("cpf"));
                    clienteEncontrado.setEmail(rs.getString("email"));
                    clienteEncontrado.setPontosDeFidelidade(rs.getInt("ponto_fidelidade"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente" + e.getMessage());
        }
        return clienteEncontrado;
    }

    public List<Cliente> listarClientes() {
        List<Cliente> listaClientes = new ArrayList<>();

        String sql = """
                SELECT * FROM cliente;
                """;

        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                Cliente cliente = new Cliente (
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getInt("ponto_fidelidade")
                );

                cliente.setId(rs.getLong("id"));
                listaClientes.add(cliente);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes." + e.getMessage());
        }
        return listaClientes;
    }

    public void deletarCliente(Long id) {
        String sql = """
                DELETE FROM cliente WHERE id = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Cliente deletado com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente." + e.getMessage());
        }
    }
}
