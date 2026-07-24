package dcx.ufpb.natan.sistemaAcai;

import java.io.*;
import java.util.Map;

public class AcaiGravador {
    public static final String NOME_ARQUIVO_PRODUTOS = "produtos.txt";

    public void salvarProdutos(Map<String, AcaiProdutos> produtos) throws IOException {
        // o ObjectOutputStream automaticamente com essa sintaxe
        try (ObjectOutputStream gravador = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO_PRODUTOS))) {
            gravador.writeObject(produtos);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, AcaiProdutos> recuperarProdutos() throws IOException {
        //  o ObjectInputStream fecha automaticamente
        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO_PRODUTOS))) {
            return (Map<String, AcaiProdutos>) leitor.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Classe desconhecida: " + e.getMessage());
        }
    }
}