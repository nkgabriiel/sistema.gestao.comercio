package sistema.gestao.controller;

import sistema.gestao.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public void registrarVenda(Venda venda) {
        String sql = """
                INSERT IGNORE INTO venda (funcionario_id, cliente_id, data, forma_pagamento, valor_total, valor_cobrado)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setLong(1, venda.getVendedor().getId());
                stmt.setLong(2, venda.getComprador().getId());
                stmt.setTimestamp(3, Timestamp.valueOf(venda.getDataHora()));
                stmt.setString(4, venda.getFormaPagamento().name());
                stmt.setDouble(5, venda.getValorTotal());
                stmt.setDouble(6, venda.getValorCobrado());
                stmt.executeUpdate();

                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if(rs.next()) {
                        venda.setId(rs.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar venda." + e.getMessage());
        }
    }

    public Venda buscarVendaPorId(Long id) {
        String sql = """
                SELECT v.*,
                f.nome AS nome_vendedor,
                c.nome AS nome_cliente
                FROM venda v
                INNER JOIN funcionario f ON v.funcionario_id = f.id
                LEFT JOIN cliente c ON v.cliente_id = c.id
                WHERE v.id = ?;
                """;

        ItemVendaDAO itemDAO = new ItemVendaDAO();

        Venda vendaEncontrada = null;

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    vendaEncontrada = new Venda();
                    vendaEncontrada.setId(rs.getLong("id"));
                    vendaEncontrada.setDataHora(rs.getTimestamp("data").toLocalDateTime());
                    vendaEncontrada.setFormaPagamento(FormaPagamento.valueOf(rs.getString("forma_pagamento")));
                    vendaEncontrada.setValorTotal(rs.getDouble("valor_total"));
                    vendaEncontrada.setValorCobrado(rs.getDouble("valor_cobrado"));

                    Funcionario f = new Funcionario();
                    f.setId(rs.getLong("funcionario_id"));
                    f.setNome(rs.getString("nome_vendedor"));
                    vendaEncontrada.setVendedor(f);

                    long idCliente = rs.getLong("cliente_id");
                    if(idCliente != 0) {
                        Cliente c = new Cliente();
                        c.setNome(rs.getString("nome_cliente"));
                        vendaEncontrada.setComprador(c);
                    }

                    List<ItemVenda> itensVenda = itemDAO.buscarItensPorVendaId(id);
                    vendaEncontrada.setItens(itensVenda);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar venda." + e.getMessage());
        }
        return vendaEncontrada;
    }

    public List<Venda> listarVendas() {
        List<Venda> vendas = new ArrayList<>();

        String sql = """
                SELECT v.*, 
                f.nome AS nome_vendedor, 
                c.nome AS nome_cliente
                FROM venda v
                INNER JOIN funcionario f ON v.funcionario_id = f.id
                LEFT JOIN cliente c ON v.cliente_id = c.id
                ORDER BY v.data_hora DESC;
                """;

        ItemVendaDAO itemDAO = new ItemVendaDAO();

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setDataHora(rs.getTimestamp("data").toLocalDateTime());
                venda.setValorTotal(rs.getDouble("valor_total"));
                venda.setValorCobrado(rs.getDouble("valor_cobrado"));
                venda.setFormaPagamento(FormaPagamento.valueOf(rs.getString("forma_pagamento")));

                Funcionario f = new Funcionario();
                f.setId(rs.getLong("funcionario_id"));
                f.setNome(rs.getString("nome_vendedor"));
                venda.setVendedor(f);

                if(rs.getLong("cliente_id") != 0) {
                    Cliente c = new Cliente();
                    c.setId(rs.getLong("cliente_id"));
                    c.setNome(rs.getString("nome_cliente"));
                    venda.setComprador(c);
                }
                List<ItemVenda> itensDaVenda = itemDAO.buscarItensPorVendaId(venda.getId());
                venda.setItens(itensDaVenda);

                vendas.add(venda);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas." + e.getMessage());
        }
        return vendas;
    }
}
