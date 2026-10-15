package br.com.mecaniqa.exception;

public class PecaNaoEncontradaException extends RuntimeException {

    public PecaNaoEncontradaException(Long codigo) {
        super("Peça não encontrada para o código: " + codigo);
    }
}