package sistema.gestao.controller;

import sistema.gestao.model.Fornecedor;
import sistema.gestao.model.ItemVenda;
import sistema.gestao.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    public void criarProduto(Produto produto) {
        String sql = """
                INSERT IGNORE INTO produto (nome, descricao, preco_custo, preco_venda, quantidade_atual, quantidade_minima, fornecedor_id)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoCusto());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getQuantidadeAtual());
            stmt.setInt(6, produto.getQuantidadeMinima());
            stmt.setLong(7, produto.getFornecedor().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar novo produto." + e.getMessage());
        }
    }

    public Produto buscarProdutoPorId(Long id) {
        String sql = """
                SELECT
                    p.*,
                    f.id AS fornecedor_id,
                    f.cnpj,
                    f.nome_representante,
                    f.contato_representante,
                    f.nome_fantasia
                FROM produto p
                INNER JOIN fornecedor f ON p.fornecedor_id = f.id
                WHERE p.id = ?;
                """;

        Produto produtoEncontrado = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produtoEncontrado = new Produto();
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setId(rs.getLong("fornecedor_id"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setNomeRepresentante(rs.getString("nome_representante"));
                    fornecedor.setContatoRepresentante(rs.getString("contato_representante"));
                    fornecedor.setNomeFantasia(rs.getString("nome_fantasia"));

                    produtoEncontrado.setId(rs.getLong("id"));
                    produtoEncontrado.setNome(rs.getString("nome"));
                    produtoEncontrado.setDescricao(rs.getString("descricao"));
                    produtoEncontrado.setPrecoCusto(rs.getDouble("preco_custo"));
                    produtoEncontrado.setPrecoVenda(rs.getDouble("preco_venda"));
                    produtoEncontrado.setQuantidadeAtual(rs.getInt("quantidade_atual"));
                    produtoEncontrado.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                    produtoEncontrado.setFornecedor(fornecedor);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto." + e.getMessage());
        }
        return produtoEncontrado;
    }

    public List<Produto> listarProdutos() {
        List<Produto> listaProdutos = new ArrayList<>();

        String sql = """
                SELECT
                    p.*,
                    f.id AS fornecedor_id,
                    f.cnpj,
                    f.nome_representante,
                    f.contato_representante,
                    f.nome_fantasia
                FROM produto p
                INNER JOIN fornecedor f ON p.fornecedor_id = f.id;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(rs.getLong("fornecedor_id"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setNomeRepresentante(rs.getString("nome_representante"));
                fornecedor.setContatoRepresentante(rs.getString("contato_representante"));
                fornecedor.setNomeFantasia(rs.getString("nome_fantasia"));

                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("preco_custo"),
                        rs.getDouble("preco_venda"),
                        rs.getInt("quantidade_atual"),
                        rs.getInt("quantidade_minima"),
                        fornecedor
                );

                produto.setId(rs.getLong("id"));
                listaProdutos.add(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos." + e.getMessage());
        }
        return listaProdutos;
    }

    public boolean atualizarProduto(Produto produto) {
        String sql = """
                UPDATE produto SET nome = ?, descricao = ?, preco_custo = ?, preco_venda = ?, 
                                   quantidade_atual = ?, quantidade_minima = ?, fornecedor_id = ?
                WHERE id = ?
                """;

        boolean produtoAtualizado = false;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoCusto());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getQuantidadeAtual());
            stmt.setInt(6, produto.getQuantidadeMinima());
            stmt.setLong(7, produto.getFornecedor().getId());
            stmt.setLong(8, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                produtoAtualizado = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto." + e.getMessage());
        }
        return produtoAtualizado;
    }

    public void deletarProduto(Long id) {
        String sql = """
                DELETE FROM produto WHERE id = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Funcionário deletado com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto." + e.getMessage());
        }
    }

    public void baixarEstoque (Integer quantidadeVendida, Long produtoId, Connection conn)  throws SQLException {
        String sql = """
                UPDATE produto SET quantidade_atual = quantidade_atual - ? WHERE id = ?;
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidadeVendida);
            stmt.setLong(2, produtoId);
            stmt.executeUpdate();
        }
    }
}
