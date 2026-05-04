package sistema.gestao.controller;

import sistema.gestao.model.ItemVenda;
import sistema.gestao.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO {
    public void salvarItemVenda(ItemVenda item, Long vendaId, Connection conn) throws SQLException {
        String sql = """
                INSERT IGNORE INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) 
                VALUES (?, ?, ?, ?);
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            stmt.setLong(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    item.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<ItemVenda> buscarItensPorVendaId (Long vendaId) {
        List<ItemVenda> listaItens = new ArrayList<>();

        String sql = """
                SELECT iv.*, p.nome AS produto_nome, p.descricao AS produto_descricao
                FROM item_venda iv
                INNER JOIN produto p ON iv.produto_id = p.id
                WHERE iv.venda_id = ?;
                """;

        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, vendaId);

           try(ResultSet rs = stmt.executeQuery()) {
               while(rs.next()) {
                   Produto p = new Produto();
                   p.setNome(rs.getString("produto_nome"));
                   p.setId(rs.getLong("produto_id"));

                   ItemVenda iv = new ItemVenda();
                   iv.setId(rs.getLong("id"));
                   iv.setQuantidade(rs.getInt("quantidade"));
                   iv.setPrecoUnitario(rs.getDouble("preco_unitario"));
                   iv.setProduto(p);

                   listaItens.add(iv);
               }
           }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens da venda." + e.getMessage());
        }
        return listaItens;
    }
}
