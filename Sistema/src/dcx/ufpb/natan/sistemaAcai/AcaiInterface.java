package dcx.ufpb.natan.sistemaAcai;

import java.io.IOException;

public interface AcaiInterface {
    /**
     * Cadastra um novo pedido no sistema de Açai.
     * @param nomeCliente O nome do cliente que faz o pedido.
     * @param idCliente O ID único do cliente usado como identificador.
     * @param funcionario O nome do funcionário que está atendendo.
     * @param categoria A categoria do produto escolhido.
     * @param produto O nome do produto/açaí selecionado.
     * @param preco O preço unitário do produto.
     * @param quantidade A quantidade de itens desejada.
     * @throws ProdutoJaExisteException Se caso ID do cliente tenha pedido criado.
     */
    public void cadastrarNovoPedido(String nomeCliente, String idCliente, String funcionario, String categoria, String produto, double preco, int quantidade) throws ProdutoJaExisteException;
    /**
     * Remove o pedido do sistema.
     * @param nomeDoPedido O nome do produto/açaí cadastrado a ser removido.
     * @throws NaoEncontradoProdutoException Caso nenhum produto com esse nome seja localizado dentro do sistema.
     */
    public void removerProdutoPeloNome(String nomeDoPedido) throws NaoEncontradoProdutoException;
    /**
     * Lista no console todos os produtos e pedidos cadastrados no sistema atual (com prints).
     */
    public void listarTodosProdutoLista();
    /**
     * Pesquisa e retorna um produto no sistema com base no nome do item informado.
     * @param nome O nome do produto a ser pesquisado.
     * @return O objeto AcaiProdutos correspondente ou null se não for encontrado.
     */
    public AcaiProdutos pegarTodosProdutosCom(String nome) throws SemProdutosException;
    /**
     * Listar no console todos os pedidos a um determinado cliente.
     * @param nomeCliente O nome do cliente para a busca.
     */
    public void listarProdutosComNomeCliente(String nomeCliente);
    /**
     * Salva todos os dados atuais em um arquivo externo.
     * @throws IOException Se ocorrer algum erro de I/O durante a gravação dos dados.
     */
    public void salvarDados() throws IOException;
    /**
     * Recupera todos os dados atuais em um arquivo externo.
     * @throws IOException Se ocorrer algum erro de I/O(Entrada e Saida) durante a gravação dos dados.
     */
    public void recuperarDados() throws IOException;

    int obterQuantidadeTotalDePedidos();

    boolean existePedidoDoCliente(String nomeCliente);

    void atualizarPrecoDoPedido(String idCliente, double novoPreco) throws NaoEncontradoProdutoException;
}