# Sistema de Gestão de Pesca

Sistema corporativo backend para gerenciamento de 
Ponto de Venda e controle de estoque de uma 
loja de artigos de pesca. Desenvolvido com foco em 
persistência relacional e orientação a objetos.

## Tecnologias Utilizadas
* **Linguagem:** Java 17+
* **Banco de Dados:** MySQL
* **Comunicação DB:** JDBC (Java Database Connectivity)
* **Interface:** CLI (Command Line Interface)

## Arquitetura do Projeto
O software foi desenhado sob o padrão arquitetural MVC (Model-View-Controller) adaptado para console, separando rigorosamente as responsabilidades:
* **Models:** Entidades de negócio (`Produto`, `Venda`, `ItemVenda`, `Funcionario`).
* **DAOs (Data Access Objects):** Isolamento das regras de persistência e transações SQL.
* **Strategy Pattern:** Utilizado na regra de negócios de métodos de pagamento, através de Interfaces e Enums integrados.

##  Funcionalidades Principais
* **Controle de Acesso:** Sessão em memória via login no banco de dados.
* **Alertas de Estoque:** Monitoramento dinâmico que avisa sobre a necessidade de reposição de mercadorias no inventário.

##  Como Executar
1. Importe o script de criação do banco de dados (`script.sql`) em sua instância MySQL.
2. Altere as credenciais (URL, USER, PASSWORD) na classe `ConnectionFactory`.
3. Compile e execute a classe `Main`.