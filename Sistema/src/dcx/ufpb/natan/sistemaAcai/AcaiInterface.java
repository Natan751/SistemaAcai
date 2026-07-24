package dcx.ufpb.natan.sistemaAcai;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface AcaiInterface {
    void cadastrarNovoPedido(String nomeCliente, String idCliente, String funcionario, String categoria, String produto, double preco, int quantidade, String dataPedido) throws ProdutoJaExisteException;
    void removerProdutoPeloNome(String nomeDoPedido) throws NaoEncontradoProdutoException;
    Collection<AcaiProdutos> listarTodosProdutos() throws SemProdutosException;
    AcaiProdutos pegarTodosProdutosCom(String nome) throws SemProdutosException;
    Collection<AcaiProdutos> listarProdutosComNomeCliente(String nomeCliente) throws SemProdutosException;
    Collection<AcaiProdutos> listarPedidosPorData(String dataPedido) throws SemProdutosException;
    Collection<AcaiProdutos> listarPedidosPorStatus(String status) throws SemProdutosException;
    void finalizarPedido(String idCliente) throws NaoEncontradoProdutoException;
    void salvarDados() throws IOException;
    void recuperarDados() throws IOException;
    int obterQuantidadeTotalDePedidos();
    boolean existePedidoDoCliente(String nomeCliente);
    void atualizarPrecoDoPedido(String idCliente, double novoPreco) throws NaoEncontradoProdutoException;
    void adicionarItemCardapio(String nomeProduto, double preco) throws ProdutoJaExisteException;
    void removerItemCardapio(String nomeProduto) throws NaoEncontradoProdutoException;
    Map<String, Double> obterCardapio() throws SemProdutosException;
}