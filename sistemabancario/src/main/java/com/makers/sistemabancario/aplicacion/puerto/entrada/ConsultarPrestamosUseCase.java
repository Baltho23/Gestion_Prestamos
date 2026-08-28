package com.makers.sistemabancario.aplicacion.puerto.entrada;

import com.makers.sistemabancario.dominio.modelo.Prestamo;

import java.util.List;

public interface ConsultarPrestamosUseCase {

    List<Prestamo> consultarPorUsuario(Long usuarioId);

    List<Prestamo> consultarTodos();
}
