package com.makers.sistemabancario.aplicacion.puerto.salida;

import com.makers.sistemabancario.dominio.modelo.Prestamo;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepositorioPuerto {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorId(Long id);

    List<Prestamo> buscarPorUsuarioId(Long usuarioId);

    List<Prestamo> buscarTodos();
}
