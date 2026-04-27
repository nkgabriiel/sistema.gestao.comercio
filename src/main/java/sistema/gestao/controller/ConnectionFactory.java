package sistema.gestao.controller;

import java.sql.*;

public class ConnectionFactory {

    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar banco de dados.", e);
        }
    }

}
