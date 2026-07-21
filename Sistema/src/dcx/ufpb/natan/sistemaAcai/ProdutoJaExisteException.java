package dcx.ufpb.natan.sistemaAcai;


public class ProdutoJaExisteException extends Exception {

    public ProdutoJaExisteException(String mensagem) {
        super(mensagem);
    }

    public ProdutoJaExisteException() {
        super("Este produto já está cadastrado no sistema.");
    }
}