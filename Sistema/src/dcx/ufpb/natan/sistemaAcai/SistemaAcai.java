package dcx.ufpb.natan.sistemaAcai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SistemaAcai implements AcaiInterface {
    private Map<String, AcaiProdutos> mapaProdutos;
    private AcaiGravador gravador;

    public SistemaAcai() {
        this.mapaProdutos = new HashMap<>();
        this.gravador = new AcaiGravador();
    }

    @Override
    public void cadastrarNovoPedido(String nomeCliente, String idCliente, String funcionario, String categoria, String produto, double preco, int quantidade) throws ProdutoJaExisteException {

        // 1. Verifica se o ID já existe ANTES de cadastrar
        if (this.mapaProdutos.containsKey(idCliente)) {
            throw new ProdutoJaExisteException("Já existe um pedido cadastrado com o ID: " + idCliente);
        }

        // 2. Se não existir, faz o cadastro normalmente
        AcaiProdutos cadastrarNovo = new AcaiProdutos(nomeCliente, idCliente, funcionario, categoria, produto, preco, quantidade);
        this.mapaProdutos.put(idCliente, cadastrarNovo);
        System.out.println("Novo pedido cadastrado com sucesso! ID: " + idCliente);
    }

    public void removerProdutoPeloNome(String nomeDoPedido) throws NaoEncontradoProdutoException {
        if (mapaProdutos.isEmpty()) {
            throw new SemProdutosException("Me desculpe, mas a lista de Produto esta vazia no momento :(");
        }

        String encontrarId = null;

        for (AcaiProdutos r : this.mapaProdutos.values()) {
            if (r.getProdutoEscolhido().equalsIgnoreCase(nomeDoPedido)) {
                encontrarId = r.getIdDoCliente();
                break;
            }

            if (encontrarId == null) {
                throw new NaoEncontradoProdutoException("Não foi encontrado o produto pra que seja removido! Nome do Produto: " + nomeDoPedido);
            }

            this.mapaProdutos.remove(encontrarId);
            System.out.println("Foi removido com sucesso o produto com nome: " + nomeDoPedido);
        }
    }

    public void listarTodosProdutoLista(){
        if (mapaProdutos.isEmpty()) {
            throw new SemProdutosException("Me desculpe, mas a lista de Produto esta vazia no momento :(");
        }

        for (AcaiProdutos p : this.mapaProdutos.values()) {
            System.out.println(p.toString());
        }
    }

    public AcaiProdutos pegarTodosProdutosCom(String nome) throws SemProdutosException {
        if (mapaProdutos.isEmpty()) {
            throw new SemProdutosException("Lista vazia!");
        }

        return mapaProdutos.values().stream()
                .filter(p -> p.getProdutoEscolhido().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public void listarProdutosComNomeCliente(String nomeCliente) {
        if (mapaProdutos.isEmpty()) {
            throw new SemProdutosException("Me desculpe, mas nao ha nenhum produto na lista.");
        }

        // Usando Stream para filtrar e imprimir
        long contagem = mapaProdutos.values().stream()
                .filter(a -> a.getNomeDoCliente().equalsIgnoreCase(nomeCliente))
                .peek(a -> System.out.println(a.toString()))
                .count();

        if (contagem == 0) {
            System.out.println("Nenhum cliente foi encontrado com o nome de " + nomeCliente);
        }
    }

    public void salvarDados() throws IOException {
        this.gravador.salvarProdutos(this.mapaProdutos);
        System.out.println("Todas as informações foram salvas com sucesso! :)");
    }

    public void recuperarDados() throws IOException {
        this.mapaProdutos = this.gravador.recuperarProdutos();
        System.out.println("Todas os dados foram recuperados com sucesso!");
    }

    @Override
    public int obterQuantidadeTotalDePedidos() {
        return mapaProdutos.size();
    }

    // 3º Uso de Stream (AnyMatch)
    @Override
    public boolean existePedidoDoCliente(String nomeCliente) {
        return mapaProdutos.values().stream()
                .anyMatch(p -> p.getNomeDoCliente().equalsIgnoreCase(nomeCliente));
    }

    @Override
    public void atualizarPrecoDoPedido(String idCliente, double novoPreco) throws NaoEncontradoProdutoException {
        if (!mapaProdutos.containsKey(idCliente)) {
            throw new NaoEncontradoProdutoException("Pedido com ID " + idCliente + " não encontrado.");
        }
        AcaiProdutos produto = mapaProdutos.get(idCliente);
        produto.setPrecoDoProduto(novoPreco);
    }

}