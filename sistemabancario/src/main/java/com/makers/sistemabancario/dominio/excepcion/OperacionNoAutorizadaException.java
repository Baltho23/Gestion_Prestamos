package com.makers.sistemabancario.dominio.excepcion;

public class OperacionNoAutorizadaException extends RuntimeException {
    public OperacionNoAutorizadaException(String mensaje) {
        super(mensaje);
    }
}
