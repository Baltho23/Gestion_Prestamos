package com.makers.sistemabancario.dominio.excepcion;

public class SaldoInvalidoException extends RuntimeException {
    public SaldoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
