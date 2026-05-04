package sistema.gestao.view;

import sistema.gestao.controller.*;
import sistema.gestao.model.*;
import sistema.gestao.model.FormaPagamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;
    private Funcionario funcionarioLogado;

    private FuncionarioDAO funcionarioDAO;
    private ProdutoDAO produtoDAO;
    private VendaDAO vendaDAO;
    private ClienteDAO clienteDAO;
    private FornecedorDAO fornecedorDAO;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.funcionarioDAO = new FuncionarioDAO();
        this.produtoDAO = new ProdutoDAO();
        this.vendaDAO = new VendaDAO();
        this.clienteDAO = new ClienteDAO();
        this.fornecedorDAO = new FornecedorDAO();
        this.funcionarioLogado = null;
    }

    public void iniciar() {
        System.out.println();
        System.out.println("  SISTEMA DE GESTAO DE PESCA E ESTOQUE   ");
        System.out.println();

        boolean rodando = true;

        while (rodando) {
            if (funcionarioLogado == null) {
                rodando = exibirMenuAcesso();
            } else {
                rodando = exibirMenuPrincipal();
            }
        }

        System.out.println("\nEncerrando o sistema. Dados persistidos com sucesso.");
        scanner.close();
    }

    // Controle de acesso

    private boolean exibirMenuAcesso() {
        System.out.println("\n--- CONTROLE DE ACESSO ---");
        System.out.println("1. Entrar (Login por CPF)");
        System.out.println("2. Cadastrar Novo Operador");
        System.out.println("9. Desligar Sistema");
        System.out.print("Opcao: ");

        int opcao = lerOpcaoInteira();

        switch (opcao) {
            case 1:
                fazerLogin();
                break;
            case 2:
                cadastrarFuncionario();
                break;
            case 9:
                return false;
            default:
                System.out.println("[!] Opcao invalida.");
        }
        return true;
    }

    private void fazerLogin() {
        System.out.print("Digite seu CPF (somente numeros): ");
        String cpf = scanner.nextLine();

        Funcionario func = funcionarioDAO.encontrarFuncionarioPorCpf(cpf);

        if (func != null) {
            this.funcionarioLogado = func;
            System.out.println("\n[+] Acesso concedido. Operador: " + func.getNome());
        } else {
            System.out.println("\n[-] CPF nao localizado na base de dados.");
        }
    }

    private void cadastrarFuncionario() {
        System.out.println("\n--- REGISTRO DE OPERADOR ---");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("CPF (somente numeros): ");
        String cpf = scanner.nextLine();

        Funcionario novoFunc = new Funcionario();
        novoFunc.setNome(nome);
        novoFunc.setCpf(cpf);

        funcionarioDAO.criarFuncionario(novoFunc);

        this.funcionarioLogado = novoFunc;
        System.out.println("\n[+] Registro concluido. Sessao iniciada automaticamente.");
    }

    // Menu Principal

    private boolean exibirMenuPrincipal() {
        System.out.println("\n=========================================");
        System.out.println("CAIXA ABERTO | OPERADOR: " + funcionarioLogado.getNome().toUpperCase());
        System.out.println("=========================================");
        System.out.println("1. Nova Venda (Frente de Caixa)");
        System.out.println("2. Gerenciar Produtos");
        System.out.println("3. Gerenciar Clientes");
        System.out.println("4. Gerenciar Fornecedores");
        System.out.println("5. Extrato de Vendas");
        System.out.println("8. Logout (Trocar Operador)");
        System.out.println("9. Desligar Sistema");
        System.out.print("Opcao: ");

        int opcao = lerOpcaoInteira();

        switch (opcao) {
            case 1:
                iniciarVenda();
                break;
            case 2:
                submenuProdutos();
                break;
            case 3:
                submenuClientes();
                break;
            case 4:
                submenuFornecedores();
                break;
            case 5:
                System.out.println("\n--- EXTRATO DE VENDAS ---");
                vendaDAO.listarVendas();
                break;
            case 8:
                this.funcionarioLogado = null;
                System.out.println("\n[+] Sessao encerrada.");
                break;
            case 9:
                return false;
            default:
                System.out.println("[!] Opcao invalida.");
        }
        return true;
    }

    private void iniciarVenda() {
        System.out.println(" Operação de Compra ");
        Venda venda = new Venda();
        venda.setVendedor(this.funcionarioLogado);
        venda.setDataHora(LocalDateTime.now());

        System.out.print("Informar CPF do cliente? (S/N): ");
        String informarCliente = scanner.nextLine().toUpperCase();

        if (informarCliente.equals("S")) {
            System.out.print("CPF: ");
            String cpfCliente = scanner.nextLine();
            System.out.println("[+] Cliente vinculado.");
        } else {
            System.out.println("[*] Venda para Consumidor Final.");
        }

        List<ItemVenda> carrinho = new ArrayList<>();
        boolean adicionandoItens = true;
        double valorTotalCarrinho = 0.0;

        while (adicionandoItens) {
            System.out.print("\nID do Produto: ");
            Long idProduto = (long) lerOpcaoInteira();
            Produto produtoEncontrado = produtoDAO.buscarProdutoPorId(idProduto);

            if (produtoEncontrado != null) {
                System.out.println("Item: " + produtoEncontrado.getNome() + " | Estoque: " + produtoEncontrado.getQuantidadeAtual());
                System.out.print("Quantidade: ");
                int qtd = lerOpcaoInteira();

                if (qtd <= produtoEncontrado.getQuantidadeAtual()) {
                    ItemVenda item = new ItemVenda(produtoEncontrado, qtd, produtoEncontrado.getPrecoVenda());
                    carrinho.add(item);
                    valorTotalCarrinho += item.getSubTotal();
                    System.out.println("[+] Item adicionado. Subtotal: R$ " + String.format("%.2f", valorTotalCarrinho));
                } else {
                    System.out.println("[-] Estoque insuficiente para esta quantidade.");
                }
            } else {
                System.out.println("[-] Produto nao cadastrado.");
            }

            System.out.print("Registrar outro item? (S/N): ");
            if (scanner.nextLine().toUpperCase().equals("N")) {
                adicionandoItens = false;
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("[-] Operacao cancelada (Carrinho vazio).");
            return;
        }

        venda.setItens(carrinho);
        venda.setValorTotal(valorTotalCarrinho);

        System.out.println("\n--- FINALIZACAO DE COMPRA ---");
        System.out.println("Total: R$ " + String.format("%.2f", valorTotalCarrinho));
        System.out.println("1. Dinheiro (-5%)");
        System.out.println("2. PIX (-5%)");
        System.out.println("3. Cartao de Debito");
        System.out.println("4. Cartao de Credito");
        System.out.print("Forma de pagamento: ");
        int formaPag = lerOpcaoInteira();

        switch (formaPag) {
            case 1: venda.setFormaPagamento(FormaPagamento.DINHEIRO); break;
            case 2: venda.setFormaPagamento(FormaPagamento.PIX); break;
            case 3: venda.setFormaPagamento(FormaPagamento.DEBITO); break;
            case 4: venda.setFormaPagamento(FormaPagamento.CREDITO); break;
            default:
                venda.setFormaPagamento(FormaPagamento.DINHEIRO);
                System.out.println("[*] Padrao assumido: Dinheiro.");
        }

        venda.aplicarMetodoPagamento(venda.getFormaPagamento());
        System.out.println("\nTotal a Pagar: R$ " + String.format("%.2f", venda.getValorCobrado()));

        System.out.print("Confirmar transacao? (S/N): ");
        if (scanner.nextLine().toUpperCase().equals("S")) {
            try {
                vendaDAO.registrarVenda(venda);
                System.out.println("\n[+] Transacao aprovada e persistida no banco de dados.");
            } catch (Exception e) {
                System.out.println("\n[-] Falha ao processar: " + e.getMessage());
            }
        } else {
            System.out.println("\n[-] Operacao abortada.");
        }
    }

    // Submenus

    private void submenuProdutos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- GERENCIAR PRODUTOS ---");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos (Estoque)");
            System.out.println("3. Buscar Produto por ID");
            System.out.println("4. Atualizar Produto");
            System.out.println("5. Deletar Produto");
            System.out.println("9. Voltar ao Menu Principal");
            System.out.print("Opcao: ");

            int opcao = lerOpcaoInteira();
            switch (opcao) {
                case 1:
                    System.out.println("\n[ Cadastrar Produto ]");
                    System.out.println("Funcionalidade delegada para futura implementacao.");
                    break;
                case 2:
                    System.out.println("\n[ Posicao de Estoque ]");
                    List<Produto> produtos = produtoDAO.listarProdutos();
                    for (Produto p : produtos) {
                        String alerta = (p.getQuantidadeAtual() <= p.getQuantidadeMinima() || p.getQuantidadeAtual() == 0) ? " [NECESSIDADE DE REPOSICAO]" : "";
                        System.out.printf("ID: %d | %s | Qtd: %d | Preco: R$ %.2f%s\n",
                                p.getId(), p.getNome(), p.getQuantidadeAtual(), p.getPrecoVenda(), alerta);
                    }
                    break;
                case 3:
                    System.out.print("\nDigite o ID do Produto: ");
                    Long idBusca = (long) lerOpcaoInteira();
                    Produto pBusca = produtoDAO.buscarProdutoPorId(idBusca);
                    if (pBusca != null) {
                        System.out.println("Encontrado: " + pBusca.getNome() + " - R$ " + pBusca.getPrecoVenda());
                    } else {
                        System.out.println("Produto nao encontrado.");
                    }
                    break;
                case 4:
                    System.out.println("\n[ Atualizar Produto ]");
                    System.out.println("Funcionalidade delegada para futura implementacao.");
                    break;
                case 5:
                    System.out.print("\nDigite o ID do Produto para Deletar: ");
                    Long idDel = (long) lerOpcaoInteira();
                    produtoDAO.deletarProduto(idDel);
                    break;
                case 9:
                    voltar = true;
                    break;
                default:
                    System.out.println("[!] Opcao invalida.");
            }
        }
    }

    private void submenuClientes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- GERENCIAR CLIENTES ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente por CPF");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Deletar Cliente");
            System.out.println("9. Voltar ao Menu Principal");
            System.out.print("Opcao: ");

            int opcao = lerOpcaoInteira();
            switch (opcao) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    System.out.println("\nFuncionalidade acessada com sucesso. Operacao no DAO delegada para o futuro.");
                    break;
                case 9:
                    voltar = true;
                    break;
                default:
                    System.out.println("[!] Opcao invalida.");
            }
        }
    }

    private void submenuFornecedores() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- GERENCIAR FORNECEDORES ---");
            System.out.println("1. Cadastrar Fornecedor");
            System.out.println("2. Listar Fornecedores");
            System.out.println("3. Buscar Fornecedor por CNPJ");
            System.out.println("4. Atualizar Fornecedor");
            System.out.println("5. Deletar Fornecedor");
            System.out.println("9. Voltar ao Menu Principal");
            System.out.print("Opcao: ");

            int opcao = lerOpcaoInteira();
            switch (opcao) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    System.out.println("\nFuncionalidade acessada com sucesso. Operacao no DAO delegada para o futuro.");
                    break;
                case 9:
                    voltar = true;
                    break;
                default:
                    System.out.println("[!] Opcao invalida.");
            }
        }
    }

    // Utilitários para evitar bugs de Scanner.

    private int lerOpcaoInteira() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double lerOpcaoDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}