package dcx.ufpb.natan.sistemaAcai;

import java.io.IOException;
import java.util.Scanner;

public class ProgramaAcai {
    public static void main(String [] args) {
        Scanner sc =  new Scanner(System.in);
        SistemaAcai sistema = new SistemaAcai();

        String menuEscolhas = "";
        String nomeDoFuncionarioAtual = null;

        // Antes de Iniciar tudo mesmo, ele vai verificar se contem Funcionario
        if (nomeDoFuncionarioAtual == null) {
            System.out.println("[ATENÇÃO SEM FUNCIONARIO]");
            System.out.println("O sistema esta sem funcionario, cadastre o nome atual do funcionario que esta usando:");
            nomeDoFuncionarioAtual = sc.nextLine();
        } else {
            System.out.println("Bem-vindo(a) " + nomeDoFuncionarioAtual);
        }

        // Sistema principal
        System.out.println("=== [GERENCIAMENTO DA LOJA DE AÇAI] ===");
        System.out.println("---------------------------------------");
        while (!menuEscolhas.equals("0")) {
            System.out.println("FUNCIONARIO NO MOMENTO: " + nomeDoFuncionarioAtual);
            System.out.println("\n1 - Cadastrar Produto\n2 - Remover Produto\n3 - Listar Todos os Produtos\n4 - Listar Todos Produtos com Nome\n5 - Todos Pedidos do Cliente\n6 - Salvar Dados dos Produtos\n7 - Recuperar Todos os Dados\n0 - Fechar o Programa");
            System.out.println("---------------------------------------");
            System.out.println("11 - Pra Trocar Funcionario no momento");
            menuEscolhas = sc.nextLine();

            if (menuEscolhas.equals("1")) {
                boolean foiConfirmado = false;

                System.out.println("Digite o nome do Cliente:");
                String perguntaNomeCliente = sc.nextLine();
                System.out.println("Digite o ID do Cliente:");
                String perguntaIDCliente = sc.nextLine();
                System.out.println("Digite a Categoria do Produto:");
                String perguntaCategoria = sc.nextLine();
                System.out.println("Digite o nome do Produto que o Cliente deseja:");
                String perguntaNomeProduto = sc.nextLine();
                System.out.println("Digite o Preço do Produto:");
                double perguntaPrecoProduto = sc.nextDouble();
                System.out.println("Digite a quantidade que o Cliente deseja:");
                int perguntaQuantProduto = sc.nextInt();
                sc.nextLine(); // Errinho chato emm de buffer

                if (foiConfirmado == false) {
                    System.out.println("Deseja confirmar esse pedido do cliente " + perguntaNomeCliente + " do produto " + perguntaNomeProduto);
                    String confirmacaoPergunta = sc.nextLine().toLowerCase();

                    if (confirmacaoPergunta.equals("s")) {
                        try {
                            sistema.cadastrarNovoPedido(perguntaNomeCliente, perguntaIDCliente, nomeDoFuncionarioAtual, perguntaCategoria, perguntaNomeProduto, perguntaPrecoProduto, perguntaQuantProduto);
                            System.out.println("Pedido foi cadastrado com sucesso! Produto: " + perguntaNomeProduto + " do Cliente: " + perguntaNomeCliente);
                            foiConfirmado = true;
                        } catch (ProdutoJaExisteException e) {
                            System.out.println("ERRO: " + e.getMessage()); // Mostra o erro se o ID já existir
                        }
                    } else {
                        System.out.println("Pedido foi cancelado! Voltando para o Menu, que peninha...");
                        break;
                    }
                }
                foiConfirmado = false;
            } else if (menuEscolhas.equals("2")) {
                System.out.println("Digite o nome do produto que deseja remover:");
                String perguntaNomeProduto = sc.nextLine();
                sistema.removerProdutoPeloNome(perguntaNomeProduto);
            } else if (menuEscolhas.equals("3")) {
                System.out.println("=== [LISTANDO TODOS OS PRODUTOS] ====");
                sistema.listarTodosProdutoLista();
            } else if (menuEscolhas.equals("4")) {
                System.out.println("Digite o nome do produto:");
                String perguntaNome = sc.nextLine();

                sistema.pegarTodosProdutosCom(perguntaNome);
            } else if (menuEscolhas.equals("5")) {
                System.out.println("Digite o nome do cliente:");
                String perguntaNomeCliente = sc.nextLine();

                sistema.listarProdutosComNomeCliente(perguntaNomeCliente);
            } else if (menuEscolhas.equals("6")) {
                try {
                    sistema.salvarDados();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (menuEscolhas.equals("7")) {
                try {
                    sistema.recuperarDados();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (menuEscolhas.equals("11")) {
                System.out.println("Digite o novo nome para o funcionario que esta usando o sistema agora:");
                nomeDoFuncionarioAtual = sc.nextLine();
            } else if (menuEscolhas.equals("0")) {
                System.out.println("Fechando o sistema... Agradeço por usar! :D");
                menuEscolhas = "0";
                break;
            } else {
                System.out.println("Invalido!");
            }
        }
        sc.close();
    }
}