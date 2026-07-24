package dcx.ufpb.natan.sistemaAcai;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SistemaAcai implements AcaiInterface {
    private Map<String, AcaiProdutos> mapaProdutos;
    private AcaiGravador gravador;
    private Map<String, Double> cardapio;

    public SistemaAcai() {
        mapaProdutos = new HashMap<>();
        gravador = new AcaiGravador();
        cardapio = new HashMap<>();
    }

    @Override
    public void adicionarItemCardapio(String nomeProduto, double preco) throws ProdutoJaExisteException {
        if (cardapio.containsKey(nomeProduto)) throw new ProdutoJaExisteException("O item '" + nomeProduto + "' já existe no cardápio.");
        cardapio.put(nomeProduto, preco);
    }

    @Override
    public void removerItemCardapio(String nomeProduto) throws NaoEncontradoProdutoException {
        if (cardapio.remove(nomeProduto) == null) {
            throw new NaoEncontradoProdutoException("O item '" + nomeProduto + "' não foi encontrado.");
        }
    }

    @Override
    public Map<String, Double> obterCardapio() throws SemProdutosException {
        if (cardapio.isEmpty()) throw new SemProdutosException("O cardápio está vazio no momento.");
        return cardapio;
    }

    @Override
    public void cadastrarNovoPedido(String nomeCliente, String idCliente, String funcionario, String categoria, String produto, double preco, int quantidade, String dataPedido) throws ProdutoJaExisteException {
        if (mapaProdutos.containsKey(idCliente)) throw new ProdutoJaExisteException("Já existe um pedido cadastrado com o ID: " + idCliente);
        mapaProdutos.put(idCliente, new AcaiProdutos(nomeCliente, idCliente, funcionario, categoria, produto, preco, quantidade, dataPedido));
    }

    @Override
    public void removerProdutoPeloNome(String nomeDoPedido) throws NaoEncontradoProdutoException {
        String idRemover = null;
        for (AcaiProdutos p : mapaProdutos.values()) {
            if (p.getProdutoEscolhido().equalsIgnoreCase(nomeDoPedido)) {
                idRemover = p.getIdDoCliente();
                break;
            }
        }
        if (idRemover == null) throw new NaoEncontradoProdutoException("Não foi encontrado o produto: " + nomeDoPedido);
        mapaProdutos.remove(idRemover);
    }

    @Override
    public Collection<AcaiProdutos> listarTodosProdutos() throws SemProdutosException {
        if (mapaProdutos.isEmpty()) throw new SemProdutosException("A lista de Produtos está vazia.");
        return mapaProdutos.values();
    }

    @Override
    public AcaiProdutos pegarTodosProdutosCom(String nome) throws SemProdutosException {
        return mapaProdutos.values().stream()
                .filter(p -> p.getProdutoEscolhido().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new SemProdutosException("Produto não encontrado!"));
    }

    @Override
    public Collection<AcaiProdutos> listarProdutosComNomeCliente(String nomeCliente) throws SemProdutosException {
        Collection<AcaiProdutos> produtos = mapaProdutos.values().stream()
                .filter(a -> a.getNomeDoCliente().equalsIgnoreCase(nomeCliente))
                .collect(Collectors.toList());

        if (produtos.isEmpty()) throw new SemProdutosException("Nenhum pedido encontrado para: " + nomeCliente);
        return produtos;
    }

    @Override
    public Collection<AcaiProdutos> listarPedidosPorData(String dataPedido) throws SemProdutosException {
        Collection<AcaiProdutos> produtos = mapaProdutos.values().stream()
                .filter(a -> a.getDataPedido().equals(dataPedido))
                .collect(Collectors.toList());

        if (produtos.isEmpty()) throw new SemProdutosException("Nenhum pedido encontrado na data: " + dataPedido);
        return produtos;
    }

    @Override
    public Collection<AcaiProdutos> listarPedidosPorStatus(String status) throws SemProdutosException {
        Collection<AcaiProdutos> produtos = mapaProdutos.values().stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());

        if (produtos.isEmpty()) throw new SemProdutosException("Nenhum pedido com o status: " + status);
        return produtos;
    }

    @Override
    public void finalizarPedido(String idCliente) throws NaoEncontradoProdutoException {
        AcaiProdutos produto = mapaProdutos.get(idCliente);
        if (produto == null) throw new NaoEncontradoProdutoException("Pedido com ID " + idCliente + " não encontrado.");
        produto.setStatus("Finalizado");
    }

    @Override
    public void salvarDados() throws IOException {
        gravador.salvarProdutos(mapaProdutos);
    }

    @Override
    public void recuperarDados() throws IOException {
        mapaProdutos = gravador.recuperarProdutos();
    }

    @Override
    public int obterQuantidadeTotalDePedidos() {
        return mapaProdutos.size();
    }

    @Override
    public boolean existePedidoDoCliente(String nomeCliente) {
        return mapaProdutos.values().stream()
                .anyMatch(p -> p.getNomeDoCliente().equalsIgnoreCase(nomeCliente));
    }

    @Override
    public void atualizarPrecoDoPedido(String idCliente, double novoPreco) throws NaoEncontradoProdutoException {
        AcaiProdutos produto = mapaProdutos.get(idCliente);
        if (produto == null) throw new NaoEncontradoProdutoException("Pedido não encontrado.");
        produto.setPrecoDoProduto(novoPreco);
    }
}