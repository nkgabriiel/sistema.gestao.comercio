package sistema.gestao;

import sistema.gestao.controller.ConnectionFactory;
import sistema.gestao.view.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            ConnectionFactory.getConnection();

            Menu menu = new Menu();
            menu.iniciar();
        } catch (Exception e) {
            System.err.println("Erro crítico: " + e.getMessage());
        }
    }
}