package dcx.ufpb.natan.sistemaAcai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SistemaAcaiTest {
    private SistemaAcai sistema;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaAcai(); // Inicia um sistema limpo antes de cada teste
    }

    @Test
    public void testCadastrarNovoPedidoComSucesso() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Açaí 500ml", 20.0, 1);
        });
        assertEquals(1, sistema.obterQuantidadeTotalDePedidos());
    }

    @Test
    public void testCadastrarPedidoDuplicadoLancaExcecao() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Açaí", 20.0, 1);
        });

        assertThrows(ProdutoJaExisteException.class, () -> {
            sistema.cadastrarNovoPedido("Carlos", "001", "Maria", "Copo", "Açaí", 15.0, 1);
        });
    }

    @Test
    public void testPesquisarProdutoPorNomeDoCliente() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Açaí 500ml", 20.0, 1);
        });

        assertTrue(sistema.existePedidoDoCliente("João"));
        assertFalse(sistema.existePedidoDoCliente("Pedro"));
    }

    @Test
    public void testRemoverProdutoComSucesso() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Acai Especial", 20.0, 1);
            sistema.removerProdutoPeloNome("Acai Especial");
        });
        assertEquals(0, sistema.obterQuantidadeTotalDePedidos());
    }

    @Test
    public void testRemoverProdutoInexistenteLancaExcecao() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Acai Normal", 20.0, 1);
        });

        assertThrows(NaoEncontradoProdutoException.class, () -> {
            sistema.removerProdutoPeloNome("Acai Especial"); // Nome errado
        });
    }
}