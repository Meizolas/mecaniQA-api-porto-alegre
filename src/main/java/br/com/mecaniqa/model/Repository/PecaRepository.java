package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.List;

public class PecaRepository {
    
    
    private static PecaRepository instance;
    
   
    private List<Peca> pecas;

   
    private PecaRepository() {
        this.pecas = new ArrayList<>();
    }

    
    public static PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }
        return instance;
    }

    
    public void adicionarPeca(Peca peca) {
        this.pecas.add(peca);
    }

    public List<Peca> listarPecas() {
        return this.pecas;
    }
}