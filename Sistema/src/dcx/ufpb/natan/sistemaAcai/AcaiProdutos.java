package dcx.ufpb.natan.sistemaAcai;

import java.io.Serializable;
import java.util.Objects;

public class AcaiProdutos implements Serializable {
    private String nomeDoCliente;
    private String idDoCliente;
    private String funcionario;
    // Parte do Produto
    private String categoria;
    private String produtoEscolhido;
    private double precoDoProduto;
    private int quantidadeProduto;

    public AcaiProdutos(String nomeDoCliente, String idDoCliente, String funcionario, String categoria, String produtoEscolhido, double precoDoProduto, int quantidadeProduto) {
        this.nomeDoCliente = nomeDoCliente;
        this.idDoCliente = idDoCliente;
        this.funcionario = funcionario;
        this.categoria = categoria;
        this.produtoEscolhido = produtoEscolhido;
        this.precoDoProduto = precoDoProduto;
        this.quantidadeProduto = quantidadeProduto;
    }

    public AcaiProdutos(){
        this("", "", "", "", "", 0.0, 0);
    }

    @Override
    public String toString() {
        return "Novo Pedido: " + this.produtoEscolhido + " da Categoria: " + this.categoria + " para " + nomeDoCliente + "(" + this.idDoCliente + ") | Funcionario: " + this.funcionario;
    }

    // Cliente
    public String getNomeDoCliente() {
        return this.nomeDoCliente;
    }

    public void setNomeDoCliente(String novoNome) {
        this.nomeDoCliente = novoNome;
    }

    // ID
    public String getIdDoCliente(){
        return this.idDoCliente;
    }

    public void setIdDoCliente(String novoID) {
        this.idDoCliente = novoID;
    }

    //Funcionario que esta atentendo o cliente
    public String getFuncionario() {
        return this.funcionario;
    }

    public void setFuncionario(String novoFuncionario) {
        this.funcionario = novoFuncionario;
    }

    //Categoria Escolhida
    public String getCategoria() {
        return this.categoria;
    }

    public void setCategoria(String novaCategoria) {
        this.categoria = novaCategoria;
    }

    //Produto escolhido pelo o Cliente
    public String getProdutoEscolhido() {
        return this.produtoEscolhido;
    }

    public void setProdutoEscolhido(String novoProduto) {
        this.produtoEscolhido = novoProduto;
    }

    // Preço do Produto
    public double getPrecoDoProduto() {
        return this.precoDoProduto;
    }

    public void setPrecoDoProduto(double novoPreco) {
        this.precoDoProduto = novoPreco;
    }

    //Quantidade
    public int getQuantidadeProduto() {
        return this.quantidadeProduto;
    }

    public void setQuantidadeProduto(int novaQuantidadeVal) {
        this.quantidadeProduto = novaQuantidadeVal;
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AcaiProdutos that = (AcaiProdutos) o;
        return Double.compare(precoDoProduto, that.precoDoProduto) == 0 && quantidadeProduto == that.quantidadeProduto && Objects.equals(nomeDoCliente, that.nomeDoCliente) && Objects.equals(idDoCliente, that.idDoCliente) && Objects.equals(funcionario, that.funcionario) && Objects.equals(categoria, that.categoria) && Objects.equals(produtoEscolhido, that.produtoEscolhido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoCliente, idDoCliente, funcionario, categoria, produtoEscolhido, precoDoProduto, quantidadeProduto);
    }
}