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
    private String dataPedido;
    private String status;

    public AcaiProdutos(String nomeDoCliente, String idDoCliente, String funcionario, String categoria, String produtoEscolhido, double precoDoProduto, int quantidadeProduto, String dataPedido) {
        this.nomeDoCliente = nomeDoCliente;
        this.idDoCliente = idDoCliente;
        this.funcionario = funcionario;
        this.categoria = categoria;
        this.produtoEscolhido = produtoEscolhido;
        this.precoDoProduto = Math.max(precoDoProduto, 0.0);
        this.quantidadeProduto = Math.max(quantidadeProduto, 1);
        this.dataPedido = dataPedido;
        this.status = "Pendente"; // Status inicial padrão
    }

    public AcaiProdutos() {
        this("", "", "", "", "", 0.0, 1, "");
    }

    @Override
    public String toString() {
        return String.format("[%s] STATUS: %s | Pedido: %s | Cat: %s | Cliente: %s (ID: %s) | Qtd: %d | Total: R$ %.2f",
                dataPedido, status.toUpperCase(), produtoEscolhido, categoria, nomeDoCliente, idDoCliente, quantidadeProduto, (precoDoProduto * quantidadeProduto));
    }

    // Getters e Setters
    public String getNomeDoCliente() { return nomeDoCliente; }
    public void setNomeDoCliente(String nomeDoCliente) { this.nomeDoCliente = nomeDoCliente; }

    public String getIdDoCliente() { return idDoCliente; }
    public void setIdDoCliente(String idDoCliente) { this.idDoCliente = idDoCliente; }

    public String getFuncionario() { return funcionario; }
    public void setFuncionario(String funcionario) { this.funcionario = funcionario; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getProdutoEscolhido() { return produtoEscolhido; }
    public void setProdutoEscolhido(String produtoEscolhido) { this.produtoEscolhido = produtoEscolhido; }

    public double getPrecoDoProduto() { return precoDoProduto; }
    public void setPrecoDoProduto(double precoDoProduto) {
        if (precoDoProduto >= 0) this.precoDoProduto = precoDoProduto;
    }

    public int getQuantidadeProduto() { return quantidadeProduto; }
    public void setQuantidadeProduto(int quantidadeProduto) {
        if (quantidadeProduto > 0) this.quantidadeProduto = quantidadeProduto;
    }

    public String getDataPedido() { return dataPedido; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcaiProdutos that = (AcaiProdutos) o;
        return Double.compare(that.precoDoProduto, precoDoProduto) == 0 && quantidadeProduto == that.quantidadeProduto && Objects.equals(nomeDoCliente, that.nomeDoCliente) && Objects.equals(idDoCliente, that.idDoCliente) && Objects.equals(funcionario, that.funcionario) && Objects.equals(categoria, that.categoria) && Objects.equals(produtoEscolhido, that.produtoEscolhido) && Objects.equals(dataPedido, that.dataPedido) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoCliente, idDoCliente, funcionario, categoria, produtoEscolhido, precoDoProduto, quantidadeProduto, dataPedido, status);
    }
}