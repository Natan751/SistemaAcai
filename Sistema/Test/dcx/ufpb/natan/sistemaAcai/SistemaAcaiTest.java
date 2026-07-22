package dcx.ufpb.natan.sistemaAcai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

public class SistemaAcaiTest {
    private SistemaAcai sistema;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaAcai();
    }

    @Test
    public void testCadastrarNovoPedidoComSucesso() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Açaí 500ml", 20.0, 1, "22/07/2026");
        });
        assertEquals(1, sistema.obterQuantidadeTotalDePedidos());
    }

    @Test
    public void testCadastrarPedidoDuplicadoLancaExcecao() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "001", "Maria", "Tigela", "Açaí 500ml", 20.0, 1, "22/07/2026");
        });

        assertThrows(ProdutoJaExisteException.class, () -> {
            sistema.cadastrarNovoPedido("Carlos", "001", "Maria", "Copo", "Açaí Simples", 15.0, 2, "22/07/2026");
        });
    }

    @Test
    public void testPesquisarProdutoPorNomeCliente() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("Natan", "002", "Maria", "Copo", "Açaí com Leite em Pó", 18.0, 1, "22/07/2026");
        });

        assertDoesNotThrow(() -> {
            Collection<AcaiProdutos> produtos = sistema.listarProdutosComNomeCliente("Natan");
            assertFalse(produtos.isEmpty());
        });
    }

    @Test
    public void testRemoverProdutoComSucesso() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("João", "003", "Maria", "Tigela", "Açaí 500ml", 20.0, 1, "22/07/2026");
            sistema.removerProdutoPeloNome("Açaí 500ml");
        });

        assertEquals(0, sistema.obterQuantidadeTotalDePedidos());
    }

    @Test
    public void testAlterarStatusParaFinalizado() {
        assertDoesNotThrow(() -> {
            sistema.cadastrarNovoPedido("Maria", "004", "João", "Tigela", "Açaí 300ml", 15.0, 1, "22/07/2026");

            sistema.finalizarPedido("004");

            Collection<AcaiProdutos> produtosFinalizados = sistema.listarPedidosPorStatus("Finalizado");
            assertEquals(1, produtosFinalizados.size());
        });
    }
}