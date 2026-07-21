package dcx.ufpb.natan.sistemaAcai;

public class NaoEncontradoProdutoException extends RuntimeException {
    public NaoEncontradoProdutoException(String message) {
        super(message);
    }
}