package com.makers.sistemabancario.aplicacion.puerto.salida;

import com.makers.sistemabancario.dominio.modelo.Usuario;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositorioPuerto {

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> buscarPorIds(Collection<Long> ids);
}
