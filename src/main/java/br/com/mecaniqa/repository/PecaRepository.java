package br.com.mecaniqa.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.config.Conexao;
import br.com.mecaniqa.mapper.PecaResultSetMapper;



public class PecaRepository extends ConsultaJdbcTemplate {

    private static PecaRepository instance;

    private final List<Peca> pecas;
    private Long proximoCodigo;

    private PecaRepository() {
        this.pecas = new ArrayList<>();
        this.proximoCodigo = 1L;
    }

    public static PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }

        return instance;
    }

    public Peca salvar(Peca peca) {
        LocalDateTime agora = LocalDateTime.now();

        peca.setCodigo(proximoCodigo++);
        peca.setDataCadastro(agora);
        peca.setDataUltimaAtualizacao(agora);

        pecas.add(peca);
        return peca;
    }

    public List<Peca> listar() {
        return new ArrayList<>(pecas);
    }

    public Optional<Peca> buscarPorCodigo(Long codigo) {
        return pecas.stream()
                .filter(peca -> peca.getCodigo().equals(codigo))
                .findFirst();
    }

    public Optional<Peca> atualizar(Long codigo, Peca novosDados) {
        Optional<Peca> pecaEncontrada = buscarPorCodigo(codigo);

        if (pecaEncontrada.isEmpty()) {
            return Optional.empty();
        }

        Peca peca = pecaEncontrada.get();

        peca.setCodigoBarras(novosDados.getCodigoBarras());
        peca.setFornecedorMarca(novosDados.getFornecedorMarca());
        peca.setQuantidadeEstoque(novosDados.getQuantidadeEstoque());
        peca.setPrecoCusto(novosDados.getPrecoCusto());
        peca.setPrecoVenda(novosDados.getPrecoVenda());
        peca.setTamanho(novosDados.getTamanho());
        peca.setCor(novosDados.getCor());
        peca.setCategoria(novosDados.getCategoria());
        peca.setDataUltimaAtualizacao(LocalDateTime.now());

        return Optional.of(peca);
    }

    public boolean excluir(Long codigo) {
        return pecas.removeIf(
                peca -> peca.getCodigo().equals(codigo)
        );
    }

    public int contar() {

        String sql = "SELECT COUNT(*) FROM pecas";

        return executarConsulta(
            sql, 
            new Consulta<Integer>() {

                @Override
                public void preencherParametros(
                    PreparedStatement statement){
                    // Esta consulta não possui parâmetros!!
                }

                @Override
                public Integer mapearResultado(
                    ResultSet resultado)
                    throws SQLException {
                    resultado.next();
                    return resultado.getInt(1);
                    }
                }
        );
    }


   public Optional<Peca> findById(Long codigo) {
    String sql = "SELECT * FROM pecas WHERE codigo = ?";

    return executarConsulta(
            sql,
            new Consulta<Optional<Peca>>() {

                @Override
                public void preencherParametros(
                        PreparedStatement statement)
                        throws SQLException {

                    statement.setLong(1, codigo);
                }

                @Override
                public Optional<Peca> mapearResultado(
                        ResultSet resultado)
                        throws SQLException {

                    if (resultado.next()) {
                        return Optional.of(
                                PecaResultSetMapper.toModel(resultado)
                        );
                    }

                    return Optional.empty();
                }
            }
    );
}
}
