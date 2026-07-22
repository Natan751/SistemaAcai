package dcx.ufpb.natan.sistemaAcai;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SistemaAcai implements AcaiInterface {
    private Map<String, AcaiProdutos> mapaProdutos;
    private AcaiGravador gravador;

    public SistemaAcai() {
        this.mapaProdutos = new HashMap<>();
        this.gravador = new AcaiGravador();
    }

    @Override
    public void cadastrarNovoPedido(String nomeCliente, String idCliente, String funcionario, String categoria, String produto, double preco, int quantidade, String dataPedido) throws ProdutoJaExisteException {
        if (this.mapaProdutos.containsKey(idCliente)) {
            throw new ProdutoJaExisteException("Já existe um pedido cadastrado com o ID: " + idCliente);
        }

        // Agora passando a dataPedido na criação do objeto
        AcaiProdutos cadastrarNovo = new AcaiProdutos(nomeCliente, idCliente, funcionario, categoria, produto, preco, quantidade, dataPedido);
        this.mapaProdutos.put(idCliente, cadastrarNovo);
    }

    @Override
    public void removerProdutoPeloNome(String nomeDoPedido) throws NaoEncontradoProdutoException {
        if (mapaProdutos.isEmpty()) {
            throw new NaoEncontradoProdutoException("A lista de Produtos está vazia no momento.");
        }

        String encontrarId = null;

        for (AcaiProdutos r : this.mapaProdutos.values()) {
            if (r.getProdutoEscolhido().equalsIgnoreCase(nomeDoPedido)) {
                encontrarId = r.getIdDoCliente();
                break;
            }
        }

        if (encontrarId == null) {
            throw new NaoEncontradoProdutoException("Não foi encontrado o produto: " + nomeDoPedido);
        }

        this.mapaProdutos.remove(encontrarId);
    }

    @Override
    public Collection<AcaiProdutos> listarTodosProdutos() throws SemProdutosException {
        if (mapaProdutos.isEmpty()) throw new SemProdutosException("A lista de Produtos está vazia.");
        return this.mapaProdutos.values();
    }

    @Override
    public AcaiProdutos pegarTodosProdutosCom(String nome) throws SemProdutosException {
        if (mapaProdutos.isEmpty()) throw new SemProdutosException("Lista vazia!");

        return mapaProdutos.values().stream()
                .filter(p -> p.getProdutoEscolhido().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<AcaiProdutos> listarProdutosComNomeCliente(String nomeCliente) throws SemProdutosException {
        if (mapaProdutos.isEmpty()) throw new SemProdutosException("Não há nenhum produto na lista.");

        Collection<AcaiProdutos> produtos = mapaProdutos.values().stream()
                .filter(a -> a.getNomeDoCliente().equalsIgnoreCase(nomeCliente))
                .collect(Collectors.toList());

        if (produtos.isEmpty()) throw new SemProdutosException("Nenhum pedido encontrado para: " + nomeCliente);
        return produtos;
    }

    // ==========================================================
    // IMPLEMENTAÇÃO DOS NOVOS MÉTODOS DE DATA E STATUS
    // ==========================================================

    @Override
    public Collection<AcaiProdutos> listarPedidosPorData(String dataPedido) throws SemProdutosException {
        if (mapaProdutos.isEmpty()) throw new SemProdutosException("Lista vazia!");

        // Uso de Stream e Filter para pegar só os da data específica
        Collection<AcaiProdutos> produtos = mapaProdutos.values().stream()
                .filter(a -> a.getDataPedido().equals(dataPedido))
                .collect(Collectors.toList());

        if (produtos.isEmpty()) throw new SemProdutosException("Nenhum pedido encontrado na data: " + dataPedido);
        return produtos;
    }

    @Override
    public Collection<AcaiProdutos> listarPedidosPorStatus(String status) throws SemProdutosException {
        if (mapaProdutos.isEmpty()) throw new SemProdutosException("Lista vazia!");

        // Uso de Stream e Filter para pegar por status (ignora se o usuario digitou maiusculo ou minusculo)
        Collection<AcaiProdutos> produtos = mapaProdutos.values().stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());

        if (produtos.isEmpty()) throw new SemProdutosException("Nenhum pedido com o status: " + status);
        return produtos;
    }

    @Override
    public void finalizarPedido(String idCliente) throws NaoEncontradoProdutoException {
        if (!mapaProdutos.containsKey(idCliente)) {
            throw new NaoEncontradoProdutoException("Pedido com ID " + idCliente + " não encontrado.");
        }
        AcaiProdutos produto = mapaProdutos.get(idCliente);
        produto.setStatus("Finalizado"); // Muda o status na hora!
    }

    // ==========================================================

    @Override
    public void salvarDados() throws IOException {
        this.gravador.salvarProdutos(this.mapaProdutos);
    }

    @Override
    public void recuperarDados() throws IOException {
        this.mapaProdutos = this.gravador.recuperarProdutos();
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
        if (!mapaProdutos.containsKey(idCliente)) throw new NaoEncontradoProdutoException("Pedido não encontrado.");
        mapaProdutos.get(idCliente).setPrecoDoProduto(novoPreco);
    }
}