package br.com.mecaniqa.repository;

import br.com.mecaniqa.config.Conexao;
import br.com.mecaniqa.model.Peca;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import br.com.mecaniqa.model.CategoriaPeca;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitarios do PecaRepository: o JDBC e simulado (Conexao mockada),
 * entao nao ha MySQL, singleton reiniciado nem dados residuais.
 * Os testes que usam o banco de verdade estao em PecaRepositoryIntegrationTest.
 */
class PecaRepositoryTest {

    private static final String SQL_EXISTE = "SELECT COUNT(*) FROM pecas WHERE codigo = ?";
    private static final String SQL_BUSCAR = "SELECT * FROM pecas WHERE codigo = ?";
    private static final String SQL_LISTAR = "SELECT * FROM pecas ORDER BY codigo";
    private static final String SQL_EXCLUIR = "DELETE FROM pecas WHERE codigo = ?";

    private PecaRepository repository;
    private Connection conexao;
    private MockedStatic<Conexao> fabrica;

    @BeforeEach
    void setUp() throws SQLException {
        repository = PecaRepository.getInstance();
        conexao = mock(Connection.class);
        fabrica = mockStatic(Conexao.class);
        fabrica.when(Conexao::getConnection).thenReturn(conexao);
    }

    @AfterEach
    void tearDown() {
        fabrica.close();
    }

    @Test
    void deveTerConstrutorPrivado() throws Exception {
        Constructor<PecaRepository> constructor = PecaRepository.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    void deveRetornarMesmaInstancia() {
        assertSame(repository, PecaRepository.getInstance());
    }

    @Test
    void existsByIdDeveConsultarPeloCodigoEFecharRecursos() throws Exception {
        PreparedStatement statement = statementComResultado(SQL_EXISTE, contagem(1));

        assertTrue(repository.existsById(7L));

        verify(statement).setLong(1, 7L);
        verify(statement).close();
        verify(conexao).close();
    }

    @Test
    void deveRetornarVazioAoBuscarCodigoInexistente() throws Exception {
        ResultSet semLinhas = mock(ResultSet.class);
        when(semLinhas.next()).thenReturn(false);
        PreparedStatement statement = statementComResultado(SQL_BUSCAR, semLinhas);

        Optional<Peca> encontrada = repository.buscarPorCodigo(999L);

        assertTrue(encontrada.isEmpty());
        verify(statement).setLong(1, 999L);
        verify(conexao).close();
    }

    @Test
    void deveListarVazioQuandoNaoHaPecas() throws Exception {
        ResultSet semLinhas = mock(ResultSet.class);
        when(semLinhas.next()).thenReturn(false);
        statementComResultado(SQL_LISTAR, semLinhas);

        List<Peca> pecas = repository.listar();

        assertTrue(pecas.isEmpty());
    }

    @Test
    void deveRetornarFalseAoExcluirCodigoInexistenteSemExecutarDelete() throws Exception {
        statementComResultado(SQL_EXISTE, contagem(0));

        assertFalse(repository.excluir(999L));

        verify(conexao, never()).prepareStatement(SQL_EXCLUIR);
    }

    @Test
    void deveExcluirQuandoCodigoExiste() throws Exception {
        statementComResultado(SQL_EXISTE, contagem(1));
        PreparedStatement delete = mock(PreparedStatement.class);
        when(conexao.prepareStatement(SQL_EXCLUIR)).thenReturn(delete);

        assertTrue(repository.excluir(7L));

        verify(delete).setLong(1, 7L);
        verify(delete).executeUpdate();
        verify(delete).close();
    }

    @Test
    void atualizarDeveRetornarVazioQuandoCodigoNaoExisteSemAlterarNada() throws Exception {
        statementComResultado(SQL_EXISTE, contagem(0));

        Optional<Peca> atualizada = repository.atualizar(999L, new Peca());

        assertTrue(atualizada.isEmpty());
        verify(conexao, never()).prepareStatement(startsWith("UPDATE"));
    }

    @Test
    void deveEncapsularFalhaDeConexaoEmRuntimeException() {
        fabrica.when(Conexao::getConnection).thenThrow(new SQLException("sem banco"));

        RuntimeException erro = assertThrows(RuntimeException.class, () -> repository.contar());

        assertInstanceOf(SQLException.class, erro.getCause());
    }

    @Test
    void salvarDeveInserirComParametrosCorretosERetornarPecaDoBanco() throws Exception {
        PreparedStatement insert = mock(PreparedStatement.class);
        ResultSet chaves = mock(ResultSet.class);
        when(insert.executeUpdate()).thenReturn(1);
        when(insert.getGeneratedKeys()).thenReturn(chaves);
        when(chaves.next()).thenReturn(true);
        when(chaves.getLong(1)).thenReturn(15L);
        when(conexao.prepareStatement(startsWith("INSERT INTO pecas"), anyInt())).thenReturn(insert);
        statementComResultado(SQL_BUSCAR, linhaDePeca(15L));

        Peca nova = new Peca();
        nova.setCodigoBarras("789000");
        nova.setPrecoVenda(40.0);
        nova.setCategoria(CategoriaPeca.FREIOS);
        Peca salva = repository.salvar(nova);

        assertEquals(15L, salva.getCodigo());
        verify(insert).setString(1, "789000");
        verify(insert).setNull(3, Types.INTEGER);
        verify(insert).setBigDecimal(5, BigDecimal.valueOf(40.0));
        verify(insert).setString(8, "FREIOS");
        verify(insert).close();
    }

    @Test
    void atualizarDeveAlterarPeloCodigoERetornarPecaDoBanco() throws Exception {
        statementComResultado(SQL_EXISTE, contagem(1));
        PreparedStatement update = mock(PreparedStatement.class);
        when(update.executeUpdate()).thenReturn(1);
        when(conexao.prepareStatement(startsWith("UPDATE pecas"))).thenReturn(update);
        statementComResultado(SQL_BUSCAR, linhaDePeca(7L));

        Peca novosDados = new Peca();
        novosDados.setCodigo(999L);
        Optional<Peca> atualizada = repository.atualizar(7L, novosDados);

        assertTrue(atualizada.isPresent());
        assertEquals(7L, atualizada.get().getCodigo());
        verify(update).setLong(9, 7L);
        verify(update).close();
    }

    @Test
    void updateDeveFalharQuandoNenhumaLinhaForAlterada() throws Exception {
        PreparedStatement update = mock(PreparedStatement.class);
        when(update.executeUpdate()).thenReturn(0);
        when(conexao.prepareStatement(startsWith("UPDATE pecas"))).thenReturn(update);
        Peca peca = new Peca();
        peca.setCodigo(7L);

        assertThrows(IllegalStateException.class, () -> repository.update(peca));
    }

    private ResultSet linhaDePeca(long codigo) throws SQLException {
        ResultSet linha = mock(ResultSet.class);
        when(linha.next()).thenReturn(true);
        when(linha.getLong("codigo")).thenReturn(codigo);
        when(linha.getString("categoria")).thenReturn("FREIOS");
        return linha;
    }

    private static String startsWith(String prefixo) {
        return argThat(sql -> sql != null && sql.trim().startsWith(prefixo));
    }

    private ResultSet contagem(int total) throws SQLException {
        ResultSet resultado = mock(ResultSet.class);
        when(resultado.next()).thenReturn(true);
        when(resultado.getInt(1)).thenReturn(total);
        return resultado;
    }

    private PreparedStatement statementComResultado(String sql, ResultSet resultado)
            throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultado);
        when(conexao.prepareStatement(sql)).thenReturn(statement);
        return statement;
    }
}
