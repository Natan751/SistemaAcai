package dcx.ufpb.natan.sistemaAcai;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class SistemaAcaiGUI extends JFrame {
    private SistemaAcai sistema;

    public SistemaAcaiGUI() {
        sistema = new SistemaAcai();
        setTitle("Sistema de Açaí - Gerenciamento Completo");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        // ================= MENU ARQUIVO =================
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSalvar = new JMenuItem("Salvar Dados");
        JMenuItem itemRecuperar = new JMenuItem("Recuperar Dados");
        JMenuItem itemSair = new JMenuItem("Sair");

        itemSalvar.addActionListener(e -> {
            try {
                sistema.salvarDados();
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso no arquivo!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemRecuperar.addActionListener(e -> {
            try {
                sistema.recuperarDados();
                JOptionPane.showMessageDialog(this, "Dados recuperados com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao recuperar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemSair.addActionListener(e -> System.exit(0));

        menuArquivo.add(itemSalvar);
        menuArquivo.add(itemRecuperar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        // ================= MENU OPERAÇÕES =================
        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemCadastrar = new JMenuItem("Cadastrar Novo Pedido");
        JMenuItem itemFinalizar = new JMenuItem("Finalizar Pedido (Mudar Status)");
        JMenuItem itemAtualizarPreco = new JMenuItem("Atualizar Preço do Pedido");
        JMenuItem itemRemover = new JMenuItem("Remover Pedido (Por Produto)");

        itemCadastrar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog(this, "ID do Cliente:");
                if (id == null) return;

                String nome = JOptionPane.showInputDialog(this, "Nome do Cliente:");
                String categoria = JOptionPane.showInputDialog(this, "Categoria (ex: Copo, Tigela):");
                String produto = JOptionPane.showInputDialog(this, "Produto Escolhido:");
                String precoStr = JOptionPane.showInputDialog(this, "Preço Unitário (ex: 15.50):");
                String qtdStr = JOptionPane.showInputDialog(this, "Quantidade:");

                // Sugere a data de hoje preenchida
                String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String data = (String) JOptionPane.showInputDialog(this, "Data do Pedido:", "Data", JOptionPane.PLAIN_MESSAGE, null, null, dataHoje);

                double preco = Double.parseDouble(precoStr.replace(",", "."));
                int qtd = Integer.parseInt(qtdStr);

                sistema.cadastrarNovoPedido(nome, id, "Atendente 1", categoria, produto, preco, qtd, data);
                JOptionPane.showMessageDialog(this, "Pedido cadastrado com sucesso! Status inicial: PENDENTE");

            } catch (ProdutoJaExisteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite números válidos para preço e quantidade.", "Erro de Digitação", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemFinalizar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog(this, "Digite o ID do pedido que deseja FINALIZAR:");
                if (id == null) return;

                sistema.finalizarPedido(id);
                JOptionPane.showMessageDialog(this, "Pedido finalizado com sucesso!");
            } catch (NaoEncontradoProdutoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        });

        itemAtualizarPreco.addActionListener(e -> {
            // ... (mesmo código de antes)
            try {
                String id = JOptionPane.showInputDialog(this, "Digite o ID do pedido que deseja alterar o preço:");
                if (id == null) return;
                String novoPrecoStr = JOptionPane.showInputDialog(this, "Digite o novo preço:");
                double novoPreco = Double.parseDouble(novoPrecoStr.replace(",", "."));
                sistema.atualizarPrecoDoPedido(id, novoPreco);
                JOptionPane.showMessageDialog(this, "Preço atualizado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: Verifique os dados digitados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemRemover.addActionListener(e -> {
            try {
                String nomeProduto = JOptionPane.showInputDialog(this, "Digite o nome exato do produto a ser removido:");
                if (nomeProduto == null) return;
                sistema.removerProdutoPeloNome(nomeProduto);
                JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        menuOperacoes.add(itemCadastrar);
        menuOperacoes.add(itemFinalizar);
        menuOperacoes.add(itemAtualizarPreco);
        menuOperacoes.add(itemRemover);

        // ================= MENU CONSULTAS =================
        JMenu menuConsultas = new JMenu("Consultas");
        JMenuItem itemListarTodos = new JMenuItem("Ver Todos os Pedidos");
        JMenuItem itemBuscarData = new JMenuItem("Filtrar Pedidos por Data");
        JMenuItem itemBuscarStatus = new JMenuItem("Filtrar por Status (Pendente/Finalizado)");

        itemListarTodos.addActionListener(e -> {
            try {
                Collection<AcaiProdutos> lista = sistema.listarTodosProdutos();
                exibirListaNaTela("TODOS OS PEDIDOS", lista);
            } catch (SemProdutosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lista Vazia", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        itemBuscarData.addActionListener(e -> {
            try {
                String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String data = (String) JOptionPane.showInputDialog(this, "Digite a data que deseja buscar:", "Filtro", JOptionPane.PLAIN_MESSAGE, null, null, dataHoje);
                if (data == null) return;

                Collection<AcaiProdutos> lista = sistema.listarPedidosPorData(data);
                exibirListaNaTela("PEDIDOS DO DIA " + data, lista);
            } catch (SemProdutosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        itemBuscarStatus.addActionListener(e -> {
            try {
                String[] opcoes = {"Pendente", "Finalizado"};
                String status = (String) JOptionPane.showInputDialog(this, "Qual status deseja buscar?", "Filtro", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
                if (status == null) return;

                Collection<AcaiProdutos> lista = sistema.listarPedidosPorStatus(status);
                exibirListaNaTela("PEDIDOS " + status.toUpperCase() + "S", lista);
            } catch (SemProdutosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menuConsultas.add(itemListarTodos);
        menuConsultas.add(itemBuscarData);
        menuConsultas.add(itemBuscarStatus);

        menuBar.add(menuArquivo);
        menuBar.add(menuOperacoes);
        menuBar.add(menuConsultas);
        setJMenuBar(menuBar);
    }

    // Método auxiliar para não repetir código na hora de mostrar o JTextArea
    private void exibirListaNaTela(String titulo, Collection<AcaiProdutos> lista) {
        StringBuilder txt = new StringBuilder("=== " + titulo + " ===\n\n");
        for (AcaiProdutos p : lista) {
            txt.append(p.toString()).append("\n\n");
        }
        JTextArea textArea = new JTextArea(txt.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaAcaiGUI().setVisible(true));
    }
}