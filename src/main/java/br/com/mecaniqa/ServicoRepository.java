package br.com.mecaniqa;

import java.util.ArrayList;
import java.util.List;

public class ServicoRepository {

    
    private static ServicoRepository instance;
    
    
    private List<Servico> servicos;

    
    private ServicoRepository() {
        this.servicos = new ArrayList<>();
    }

    
    public static ServicoRepository getInstance() {
        if (instance == null) {
            instance = new ServicoRepository();
        }
        return instance;
    }

    
    public void adicionarServico(Servico servico) {
        this.servicos.add(servico);
    }

    public List<Servico> listarServicos() {
        return this.servicos;
    }
}