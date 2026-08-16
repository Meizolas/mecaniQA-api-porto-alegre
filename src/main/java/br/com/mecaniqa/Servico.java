package src.main.java.br.com.mecaniqa;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Servico {
    private Long id;
    private String descricao;
    private Double valorMaoDeObra;
    private Double custoTabelado;
    private Double tempoEstimadoMinutos;
    private List<Peca> pecasUtilazadas;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public Servico() {
        this.pecasUtilazadas = new ArrayList<>();
    }

    public Servico(Long id, String descricao, Double valorMaoDeObra, Double custoTabelado, Double tempoEstimadoMinutos, List<Peca> pecasUtilazadas, LocalDateTime dataCadastro, LocalDateTime dataAtualizacao) {
        this();
        this.id = id;
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
        this.custoTabelado = custoTabelado;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.pecasUtilazadas = new ArrayList<>();
        this.dataCadastro = dataCadastro;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorMaoDeObra() {
        return valorMaoDeObra;
    }

    public void setValorMaoDeObra(Double valorMaoDeObra) {
        this.valorMaoDeObra = valorMaoDeObra;
    }

    public Double getCustoTabelado() {
        return custoTabelado;
    }

    public void setCustoTabelado(Double custoTabelado) {
        this.custoTabelado = custoTabelado;
    }

    public Double gettempoEstimadoMinutos() {
        return tempoEstimadoMinutos;
    }

    public void settempoEstimadoMinutos(Double tempoEstimadoMinutos) {
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
    }

    public List<Peca> getPecasUtilizadas() {
        return pecasUtilazadas;
    }

    public void setPecasUtilizadas(List<Peca> pecasUtilazadas) {
        this.pecasUtilazadas = pecasUtilazadas;
    }


    public void adicionarPeca(Peca peca) {
        this.pecasUtilazadas.add(peca);
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
