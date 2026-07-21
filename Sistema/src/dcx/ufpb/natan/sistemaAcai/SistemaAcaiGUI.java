package dcx.ufpb.natan.sistemaAcai;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SistemaAcaiGUI extends JFrame {
    private SistemaAcai sistema;

    public SistemaAcaiGUI() {
        sistema = new SistemaAcai();
        setTitle("Sistema de Açaí");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando a Barra de Menu
        JMenuBar menuBar = new JMenuBar();

        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSalvar = new JMenuItem("Salvar Dados");
        JMenuItem itemRecuperar = new JMenuItem("Recuperar Dados");
        JMenuItem itemSair = new JMenuItem("Sair");

        itemSalvar.addActionListener(e -> {
            try {
                sistema.salvarDados();
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        itemRecuperar.addActionListener(e -> {
            try {
                sistema.recuperarDados();
                JOptionPane.showMessageDialog(this, "Dados recuperados com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao recuperar: " + ex.getMessage());
            }
        });

        itemSair.addActionListener(e -> System.exit(0));

        menuArquivo.add(itemSalvar);
        menuArquivo.add(itemRecuperar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        // Menu Operações
        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemCadastrar = new JMenuItem("Cadastrar Pedido");
        JMenuItem itemQtd = new JMenuItem("Total de Pedidos");

        itemCadastrar.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog("ID do Cliente:");
                String nome = JOptionPane.showInputDialog("Nome do Cliente:");
                String produto = JOptionPane.showInputDialog("Produto:");
                sistema.cadastrarNovoPedido(nome, id, "Funcionario1", "Copo", produto, 15.0, 1);
                JOptionPane.showMessageDialog(this, "Cadastrado com sucesso!");
            } catch (ProdutoJaExisteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemQtd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Total de pedidos: " + sistema.obterQuantidadeTotalDePedidos());
        });

        menuOperacoes.add(itemCadastrar);
        menuOperacoes.add(itemQtd);

        menuBar.add(menuArquivo);
        menuBar.add(menuOperacoes);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaAcaiGUI().setVisible(true));
    }
}