package dcx.ufpb.natan.sistemaAcai;

import java.io.Serializable;
import java.util.Objects;

public class AcaiProdutos implements Serializable {
    private String nomeDoCliente;
    private String idDoCliente;
    private String funcionario;
    private String categoria;
    private String produtoEscolhido;
    private double precoDoProduto;
    private int quantidadeProduto;

    // --- NOVOS ATRIBUTOS ---
    private String dataPedido;
    private String status; // Vai guardar "Pendente" ou "Finalizado"

    public AcaiProdutos(String nomeDoCliente, String idDoCliente, String funcionario, String categoria, String produtoEscolhido, double precoDoProduto, int quantidadeProduto, String dataPedido) {
        this.nomeDoCliente = nomeDoCliente;
        this.idDoCliente = idDoCliente;
        this.funcionario = funcionario;
        this.categoria = categoria;
        this.produtoEscolhido = produtoEscolhido;
        this.precoDoProduto = Math.max(precoDoProduto, 0.0);
        this.quantidadeProduto = Math.max(quantidadeProduto, 1);

        // Inicializando os novos atributos
        this.dataPedido = dataPedido;
        this.status = "Pendente"; // Por padrão, todo pedido novo começa como pendente
    }

    public AcaiProdutos(){
        this("", "", "", "", "", 0.0, 1, "");
        this.status = "Pendente";
    }

    @Override
    public String toString() {
        return String.format("[%s] STATUS: %s | Pedido: %s | Cat: %s | Cliente: %s (ID: %s) | Qtd: %d | Total: R$ %.2f",
                dataPedido, status.toUpperCase(), produtoEscolhido, categoria, nomeDoCliente, idDoCliente, quantidadeProduto, (precoDoProduto * quantidadeProduto));
    }

    // --- GETTERS E SETTERS ---

    public String getNomeDoCliente() { return this.nomeDoCliente; }
    public void setNomeDoCliente(String novoNome) { this.nomeDoCliente = novoNome; }

    public String getIdDoCliente() { return this.idDoCliente; }
    public void setIdDoCliente(String novoID) { this.idDoCliente = novoID; }

    public String getFuncionario() { return this.funcionario; }
    public void setFuncionario(String novoFuncionario) { this.funcionario = novoFuncionario; }

    public String getCategoria() { return this.categoria; }
    public void setCategoria(String novaCategoria) { this.categoria = novaCategoria; }

    public String getProdutoEscolhido() { return this.produtoEscolhido; }
    public void setProdutoEscolhido(String novoProduto) { this.produtoEscolhido = novoProduto; }

    public double getPrecoDoProduto() { return this.precoDoProduto; }
    public void setPrecoDoProduto(double novoPreco) {
        if (novoPreco >= 0) this.precoDoProduto = novoPreco;
    }

    public int getQuantidadeProduto() { return this.quantidadeProduto; }
    public void setQuantidadeProduto(int novaQuantidadeVal) {
        if (novaQuantidadeVal > 0) this.quantidadeProduto = novaQuantidadeVal;
    }

    // Novos Getters e Setters
    public String getDataPedido() { return dataPedido; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- EQUALS E HASHCODE ---
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AcaiProdutos that = (AcaiProdutos) o;
        return Double.compare(precoDoProduto, that.precoDoProduto) == 0 && quantidadeProduto == that.quantidadeProduto && Objects.equals(nomeDoCliente, that.nomeDoCliente) && Objects.equals(idDoCliente, that.idDoCliente) && Objects.equals(funcionario, that.funcionario) && Objects.equals(categoria, that.categoria) && Objects.equals(produtoEscolhido, that.produtoEscolhido) && Objects.equals(dataPedido, that.dataPedido) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoCliente, idDoCliente, funcionario, categoria, produtoEscolhido, precoDoProduto, quantidadeProduto, dataPedido, status);
    }
}