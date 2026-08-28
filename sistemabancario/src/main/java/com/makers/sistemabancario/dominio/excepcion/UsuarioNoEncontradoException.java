package com.makers.sistemabancario.dominio.excepcion;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UsuarioNoEncontradoException(String email) {
        super("Usuario no encontrado: " + email);
    }
}
