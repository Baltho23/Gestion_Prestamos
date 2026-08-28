package com.makers.sistemabancario.dominio.excepcion;

public class PlazoInvalidoException extends RuntimeException {
    public PlazoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
