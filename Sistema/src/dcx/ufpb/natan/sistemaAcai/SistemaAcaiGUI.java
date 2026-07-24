package dcx.ufpb.natan.sistemaAcai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SistemaAcaiGUI extends JFrame {
    private final SistemaAcai sistema = new SistemaAcai();
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID Pedido", "Cliente", "Produto (Adicionais)", "Qtd", "Total (R$)", "Status"}, 0
    );

    public SistemaAcaiGUI() {
        setTitle("Sistema de Açaí - Frente de Caixa (PDV)");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTable tabelaPedidos = new JTable(modeloTabela);
        tabelaPedidos.setRowHeight(25);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createTitledBorder("Fila de Preparo (Pedidos Pendentes)"));
        painelCentral.add(new JScrollPane(tabelaPedidos), BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);

        setJMenuBar(criarBarraMenu());

        atualizarTabelaDashboard();
    }

    private JMenuBar criarBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(criarMenuArquivo());
        menuBar.add(criarMenuCardapio());
        menuBar.add(criarMenuOperacoes());
        return menuBar;
    }

    private JMenu criarMenuArquivo() {
        JMenu menu = new JMenu("Arquivo");

        JMenuItem itemSalvar = new JMenuItem("Salvar Dados");
        itemSalvar.addActionListener(e -> {
            try {
                sistema.salvarDados();
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso no arquivo!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem itemRecuperar = new JMenuItem("Recuperar Dados");
        itemRecuperar.addActionListener(e -> {
            try {
                sistema.recuperarDados();
                atualizarTabelaDashboard();
                JOptionPane.showMessageDialog(this, "Dados recuperados com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao recuperar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));

        menu.add(itemSalvar);
        menu.add(itemRecuperar);
        menu.addSeparator();
        menu.add(itemSair);
        return menu;
    }

    private JMenu criarMenuCardapio() {
        JMenu menu = new JMenu("Cardápio");

        JMenuItem itemVer = new JMenuItem("Ver Cardápio");
        itemVer.addActionListener(e -> {
            try {
                Map<String, Double> itens = sistema.obterCardapio();
                StringBuilder sb = new StringBuilder("=== NOSSO CARDÁPIO ===\n\n");
                itens.forEach((nome, preco) -> sb.append(String.format(">> %s ......... R$ %.2f\n", nome, preco)));
                JOptionPane.showMessageDialog(this, sb.toString(), "Cardápio Atual", JOptionPane.INFORMATION_MESSAGE);
            } catch (SemProdutosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        JMenuItem itemAdd = new JMenuItem("Adicionar Item ao Cardápio");
        itemAdd.addActionListener(e -> {
            try {
                String nome = JOptionPane.showInputDialog(this, "Nome do novo Produto:");
                if (nome == null || nome.trim().isEmpty()) return;
                String precoStr = JOptionPane.showInputDialog(this, "Preço (ex: 15.50):");
                if (precoStr == null) return;
                double preco = Double.parseDouble(precoStr.replace(",", "."));
                sistema.adicionarItemCardapio(nome, preco);
                JOptionPane.showMessageDialog(this, "Item adicionado ao cardápio!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar item. Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem itemRemover = new JMenuItem("Remover Item do Cardápio");
        itemRemover.addActionListener(e -> {
            try {
                String nome = JOptionPane.showInputDialog(this, "Nome exato do item a remover:");
                if (nome != null) {
                    sistema.removerItemCardapio(nome);
                    JOptionPane.showMessageDialog(this, "Item removido!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        menu.add(itemVer);
        menu.add(itemAdd);
        menu.add(itemRemover);
        return menu;
    }

    private JMenu criarMenuOperacoes() {
        JMenu menu = new JMenu("Operações");

        JMenuItem itemCadastrar = new JMenuItem("Abrir Novo Pedido (Frente de Caixa)");
        itemCadastrar.addActionListener(e -> cadastrarNovoPedidoGUI());

        JMenuItem itemFinalizar = new JMenuItem("Cobrar e Finalizar Pedido");
        itemFinalizar.addActionListener(e -> finalizarPedidoGUI());

        JMenuItem itemRemover = new JMenuItem("Cancelar/Remover Pedido");
        itemRemover.addActionListener(e -> removerPedidoGUI());

        menu.add(itemCadastrar);
        menu.add(itemFinalizar);
        menu.add(itemRemover);
        return menu;
    }

    private void cadastrarNovoPedidoGUI() {
        try {
            Map<String, Double> cardapioAtual = sistema.obterCardapio();
            String[] arrayCardapio = cardapioAtual.keySet().toArray(new String[0]);

            String idBase = JOptionPane.showInputDialog(this, "Número/Mesa do Pedido (Ex: 001):");
            if (idBase == null || idBase.trim().isEmpty()) return;

            String nome = JOptionPane.showInputDialog(this, "Nome do Cliente:");
            if (nome == null) return;

            String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            boolean continuar = true;
            int contadorItem = 1;
            double valorTotalPedido = 0.0;

            StringBuilder cupom = new StringBuilder();
            cupom.append("========================================\n")
                    .append("            AÇAÍ DO NATAN               \n")
                    .append("           JOÃO PESSOA - PB             \n")
                    .append("          CUPOM NÃO FISCAL              \n")
                    .append("========================================\n")
                    .append("Cliente: ").append(nome).append("\n")
                    .append("Data: ").append(data).append("\n")
                    .append("Pedido: ").append(idBase).append("\n")
                    .append("----------------------------------------\n");

            while (continuar) {
                String produtoEscolhido = (String) JOptionPane.showInputDialog(
                        this, "Escolha a base:", "Item " + contadorItem,
                        JOptionPane.QUESTION_MESSAGE, null, arrayCardapio, arrayCardapio[0]
                );
                if (produtoEscolhido == null) break;

                double precoFinal = cardapioAtual.get(produtoEscolhido);
                String descProdutoFinal = produtoEscolhido;

                // Painel de complementos
                JPanel painelAdds = new JPanel(new GridLayout(0, 1));
                JCheckBox cbNinho = new JCheckBox("Leite Ninho (Grátis)");
                JCheckBox cbGranola = new JCheckBox("Granola (Grátis)");
                JCheckBox cbMorango = new JCheckBox("Morango Fresco (+ R$ 3.00)");
                JCheckBox cbNutella = new JCheckBox("Nutella (+ R$ 5.00)");

                painelAdds.add(new JLabel("Escolha os acompanhamentos:"));
                painelAdds.add(cbNinho);
                painelAdds.add(cbGranola);
                painelAdds.add(cbMorango);
                painelAdds.add(cbNutella);

                if (JOptionPane.showConfirmDialog(this, painelAdds, "Adicionais para " + produtoEscolhido, JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
                    break;
                }

                List<String> adds = new ArrayList<>();
                if (cbNinho.isSelected()) adds.add("Leite Ninho");
                if (cbGranola.isSelected()) adds.add("Granola");
                if (cbMorango.isSelected()) { adds.add("Morango"); precoFinal += 3.00; }
                if (cbNutella.isSelected()) { adds.add("Nutella"); precoFinal += 5.00; }

                if (!adds.isEmpty()) {
                    descProdutoFinal += " (" + String.join(", ", adds) + ")";
                }

                String qtdStr = JOptionPane.showInputDialog(this, "Quantidade:", "1");
                if (qtdStr == null) break;
                int qtd = Integer.parseInt(qtdStr);

                String idFinal = idBase + "-" + contadorItem;
                sistema.cadastrarNovoPedido(nome, idFinal, "Caixa 1", "Açaí/Sobremesa", descProdutoFinal, precoFinal, qtd, data);

                double subtotal = precoFinal * qtd;
                valorTotalPedido += subtotal;
                cupom.append(String.format("%dx %s \n   -> R$ %.2f\n", qtd, descProdutoFinal, subtotal));

                int resposta = JOptionPane.showConfirmDialog(this, "Item adicionado! Deseja adicionar MAIS itens a este pedido?", "Mais itens?", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    contadorItem++;
                } else {
                    continuar = false;
                }
            }

            if (contadorItem > 1 || !continuar) {
                cupom.append("----------------------------------------\n")
                        .append(String.format("TOTAL A PAGAR:            R$ %.2f\n", valorTotalPedido))
                        .append("========================================\n");

                JTextArea txtCupom = new JTextArea(cupom.toString());
                txtCupom.setFont(new Font("Monospaced", Font.PLAIN, 14));
                txtCupom.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(txtCupom), "Pedido Cadastrado!", JOptionPane.INFORMATION_MESSAGE);

                atualizarTabelaDashboard();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ação interrompida ou dados inválidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void finalizarPedidoGUI() {
        try {
            String id = JOptionPane.showInputDialog(this, "Digite o ID exato do pedido (ex: 001-1) para COBRAR:");
            if (id == null) return;

            double valorCobrar = 0.0;
            boolean achou = false;

            try {
                for (AcaiProdutos p : sistema.listarTodosProdutos()) {
                    if (p.getIdDoCliente().equals(id) && p.getStatus().equalsIgnoreCase("Pendente")) {
                        valorCobrar = p.getPrecoDoProduto() * p.getQuantidadeProduto();
                        achou = true;
                        break;
                    }
                }
            } catch (Exception ignored) {}

            if (achou) {
                String[] pagamentos = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
                String formaPagamento = (String) JOptionPane.showInputDialog(
                        this, String.format("Total a pagar: R$ %.2f\nQual a forma de pagamento?", valorCobrar),
                        "Pagamento", JOptionPane.QUESTION_MESSAGE, null, pagamentos, pagamentos[0]
                );

                if (formaPagamento == null) return;

                if ("Dinheiro".equals(formaPagamento)) {
                    String entregueStr = JOptionPane.showInputDialog(this, "Cliente entregou qual valor em dinheiro?");
                    if (entregueStr != null) {
                        double entregue = Double.parseDouble(entregueStr.replace(",", "."));
                        double troco = entregue - valorCobrar;
                        if (troco < 0) {
                            JOptionPane.showMessageDialog(this, "Valor insuficiente! Operação cancelada.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        JOptionPane.showMessageDialog(this, String.format("Troco a devolver: R$ %.2f", troco), "Entregue o Troco", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        return;
                    }
                }
            }

            sistema.finalizarPedido(id);
            JOptionPane.showMessageDialog(this, "Pedido finalizado e entregue com sucesso!");
            atualizarTabelaDashboard();

        } catch (NaoEncontradoProdutoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro no pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerPedidoGUI() {
        try {
            String nomeProduto = JOptionPane.showInputDialog(this, "Digite o nome exato do produto a ser removido (com adicionais se houver):");
            if (nomeProduto != null) {
                sistema.removerProdutoPeloNome(nomeProduto);
                JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
                atualizarTabelaDashboard();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTabelaDashboard() {
        modeloTabela.setRowCount(0);
        try {
            for (AcaiProdutos p : sistema.listarPedidosPorStatus("Pendente")) {
                modeloTabela.addRow(new Object[]{
                        p.getIdDoCliente(),
                        p.getNomeDoCliente(),
                        p.getProdutoEscolhido(),
                        p.getQuantidadeProduto(),
                        String.format("R$ %.2f", p.getPrecoDoProduto() * p.getQuantidadeProduto()),
                        p.getStatus()
                });
            }
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new SistemaAcaiGUI().setVisible(true));
    }
}