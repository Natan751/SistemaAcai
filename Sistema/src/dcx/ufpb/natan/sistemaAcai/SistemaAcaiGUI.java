package dcx.ufpb.natan.sistemaAcai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SistemaAcaiGUI extends JFrame {
    private SistemaAcai sistema;
    private JTable tabelaPedidos;
    private DefaultTableModel modeloTabela;

    public SistemaAcaiGUI() {
        sistema = new SistemaAcai();
        setTitle("Sistema de Açaí - Frente de Caixa (PDV)");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= DASHBOARD (FILA DE PREPARO) =================
        String[] colunas = {"ID Pedido", "Cliente", "Produto (Adicionais)", "Qtd", "Total (R$)", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaPedidos = new JTable(modeloTabela);
        tabelaPedidos.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createTitledBorder("Fila de Preparo (Pedidos Pendentes)"));
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);

        // ================= MENUS DA APLICAÇÃO =================
        JMenuBar menuBar = new JMenuBar();

        // --- MENU ARQUIVO ---
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
                atualizarTabelaDashboard(); // Atualiza a tela ao carregar
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

        // --- MENU CARDÁPIO ---
        JMenu menuCardapio = new JMenu("Cardápio");
        JMenuItem itemVerCardapio = new JMenuItem("Ver Cardápio");
        JMenuItem itemAddCardapio = new JMenuItem("Adicionar Item ao Cardápio");
        JMenuItem itemRemoverCardapio = new JMenuItem("Remover Item do Cardápio");

        itemVerCardapio.addActionListener(e -> {
            try {
                Map<String, Double> itens = sistema.obterCardapio();
                StringBuilder sb = new StringBuilder("=== NOSSO CARDÁPIO ===\n\n");
                for (Map.Entry<String, Double> entry : itens.entrySet()) {
                    sb.append(String.format(">> %s ......... R$ %.2f\n", entry.getKey(), entry.getValue()));
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Cardápio Atual", JOptionPane.INFORMATION_MESSAGE);
            } catch (SemProdutosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        itemAddCardapio.addActionListener(e -> {
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

        itemRemoverCardapio.addActionListener(e -> {
            try {
                String nome = JOptionPane.showInputDialog(this, "Nome exato do item a remover:");
                if (nome == null) return;
                sistema.removerItemCardapio(nome);
                JOptionPane.showMessageDialog(this, "Item removido!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        menuCardapio.add(itemVerCardapio);
        menuCardapio.add(itemAddCardapio);
        menuCardapio.add(itemRemoverCardapio);

        // --- MENU OPERAÇÕES ---
        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemCadastrar = new JMenuItem("Abrir Novo Pedido (Frente de Caixa)");
        JMenuItem itemFinalizar = new JMenuItem("Cobrar e Finalizar Pedido");
        JMenuItem itemRemover = new JMenuItem("Cancelar/Remover Pedido");

        itemCadastrar.addActionListener(e -> {
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

                // INÍCIO DO CUPOM NÃO FISCAL
                StringBuilder cupom = new StringBuilder();
                cupom.append("========================================\n");
                cupom.append("            AÇAÍ DO NATAN               \n");
                cupom.append("           JOÃO PESSOA - PB             \n");
                cupom.append("          CUPOM NÃO FISCAL              \n");
                cupom.append("========================================\n");
                cupom.append("Cliente: ").append(nome).append("\n");
                cupom.append("Data: ").append(data).append("\n");
                cupom.append("Pedido: ").append(idBase).append("\n");
                cupom.append("----------------------------------------\n");

                while (continuar) {
                    String produtoEscolhido = (String) JOptionPane.showInputDialog(this, "Escolha a base:", "Item " + contadorItem, JOptionPane.QUESTION_MESSAGE, null, arrayCardapio, arrayCardapio[0]);
                    if (produtoEscolhido == null) break;

                    double precoBase = cardapioAtual.get(produtoEscolhido);
                    double precoFinal = precoBase;
                    String descProdutoFinal = produtoEscolhido;

                    // SISTEMA DE ADICIONAIS
                    JPanel painelAdicionais = new JPanel(new GridLayout(0, 1));
                    JCheckBox cbLeiteNinho = new JCheckBox("Leite Ninho (Grátis)");
                    JCheckBox cbGranola = new JCheckBox("Granola (Grátis)");
                    JCheckBox cbMorango = new JCheckBox("Morango Fresco (+ R$ 3.00)");
                    JCheckBox cbNutella = new JCheckBox("Nutella (+ R$ 5.00)");

                    painelAdicionais.add(new JLabel("Escolha os acompanhamentos:"));
                    painelAdicionais.add(cbLeiteNinho);
                    painelAdicionais.add(cbGranola);
                    painelAdicionais.add(cbMorango);
                    painelAdicionais.add(cbNutella);

                    int result = JOptionPane.showConfirmDialog(this, painelAdicionais, "Adicionais para " + produtoEscolhido, JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        List<String> adds = new ArrayList<>();
                        if (cbLeiteNinho.isSelected()) adds.add("Leite Ninho");
                        if (cbGranola.isSelected()) adds.add("Granola");
                        if (cbMorango.isSelected()) { adds.add("Morango"); precoFinal += 3.00; }
                        if (cbNutella.isSelected()) { adds.add("Nutella"); precoFinal += 5.00; }

                        if (!adds.isEmpty()) {
                            descProdutoFinal += " (" + String.join(", ", adds) + ")";
                        }
                    } else {
                        break;
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
                    cupom.append("----------------------------------------\n");
                    cupom.append(String.format("TOTAL A PAGAR:            R$ %.2f\n", valorTotalPedido));
                    cupom.append("========================================\n");

                    JTextArea txtCupom = new JTextArea(cupom.toString());
                    txtCupom.setFont(new Font("Monospaced", Font.PLAIN, 14));
                    txtCupom.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(txtCupom), "Pedido Cadastrado!", JOptionPane.INFORMATION_MESSAGE);

                    atualizarTabelaDashboard();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ação interrompida ou dados inválidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        itemFinalizar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog(this, "Digite o ID exato do pedido (ex: 001-1) para COBRAR:");
                if (id == null) return;

                double valorCobrar = 0.0;
                boolean achou = false;

                try {
                    Collection<AcaiProdutos> todos = sistema.listarTodosProdutos();
                    for(AcaiProdutos p : todos) {
                        // AJUSTADO: Usando os Getters da sua classe AcaiProdutos
                        if (p.getIdDoCliente().equals(id) && p.getStatus().equalsIgnoreCase("Pendente")) {
                            valorCobrar = p.getPrecoDoProduto() * p.getQuantidadeProduto();
                            achou = true;
                            break;
                        }
                    }
                } catch (Exception ignore) {}

                if (achou) {
                    String[] pagamentos = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
                    String formaPagamento = (String) JOptionPane.showInputDialog(this,
                            String.format("Total a pagar: R$ %.2f\nQual a forma de pagamento?", valorCobrar),
                            "Pagamento", JOptionPane.QUESTION_MESSAGE, null, pagamentos, pagamentos[0]);

                    if (formaPagamento == null) return;

                    if (formaPagamento.equals("Dinheiro")) {
                        String entregueStr = JOptionPane.showInputDialog(this, "Cliente entregou qual valor em dinheiro?");
                        if (entregueStr != null) {
                            double entregue = Double.parseDouble(entregueStr.replace(",", "."));
                            double troco = entregue - valorCobrar;
                            if (troco < 0) {
                                JOptionPane.showMessageDialog(this, "Valor insuficiente! Operação cancelada.", "Erro", JOptionPane.ERROR_MESSAGE);
                                return;
                            } else {
                                JOptionPane.showMessageDialog(this, String.format("Troco a devolver: R$ %.2f", troco), "Entregue o Troco", JOptionPane.INFORMATION_MESSAGE);
                            }
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
        });

        itemRemover.addActionListener(e -> {
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
        });

        menuOperacoes.add(itemCadastrar);
        menuOperacoes.add(itemFinalizar);
        menuOperacoes.add(itemRemover);

        // Adicionando tudo à barra
        menuBar.add(menuArquivo);
        menuBar.add(menuCardapio);
        menuBar.add(menuOperacoes);
        setJMenuBar(menuBar);

        atualizarTabelaDashboard();
    }

    private void atualizarTabelaDashboard() {
        modeloTabela.setRowCount(0);
        try {
            Collection<AcaiProdutos> pedidos = sistema.listarPedidosPorStatus("Pendente");
            for (AcaiProdutos p : pedidos) {
                // AJUSTADO: Aqui estão os Getters exatos da sua classe AcaiProdutos
                modeloTabela.addRow(new Object[]{
                        p.getIdDoCliente(),
                        p.getNomeDoCliente(),
                        p.getProdutoEscolhido(),
                        p.getQuantidadeProduto(),
                        String.format("R$ %.2f", p.getPrecoDoProduto() * p.getQuantidadeProduto()),
                        p.getStatus()
                });
            }
        } catch (SemProdutosException e) {
            // Silencioso
        } catch (Exception e) {
            // Silencioso
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new SistemaAcaiGUI().setVisible(true));
    }
}