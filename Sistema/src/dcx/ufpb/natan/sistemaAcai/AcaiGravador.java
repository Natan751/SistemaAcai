package dcx.ufpb.natan.sistemaAcai;


import java.io.*;
import java.util.Map;

public class AcaiGravador {
    public static final String NOME_ARQUIVO_PRODUTOS = "produtos.txt";

    public void salvarProdutos(Map<String, dcx.ufpb.natan.sistemaAcai.AcaiProdutos> produtos) throws IOException {
        File arquivo = new File(NOME_ARQUIVO_PRODUTOS);

        ObjectOutputStream gravador = null;
        try {
            gravador = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO_PRODUTOS));
            gravador.writeObject(produtos);
        } finally {
            if (gravador!=null){
                gravador.close();
            }
        }
    }

    public Map<String, dcx.ufpb.natan.sistemaAcai.AcaiProdutos> recuperarProdutos() throws IOException {
        ObjectInputStream leitor = null;
        try {
            leitor = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO_PRODUTOS));
            Map<String, AcaiProdutos> produtosRecuperados = (Map<String, dcx.ufpb.natan.sistemaAcai.AcaiProdutos>) leitor.readObject();

            return produtosRecuperados;
        } catch(ClassNotFoundException e) {
            throw new IOException("Classe desconhecida: " + e.getMessage());
        } finally {
            if (leitor != null) {
                leitor.close();
            }
        }

    }
}