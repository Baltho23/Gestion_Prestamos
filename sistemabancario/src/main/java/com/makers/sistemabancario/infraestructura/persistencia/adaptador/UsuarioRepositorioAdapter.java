package com.makers.sistemabancario.infraestructura.persistencia.adaptador;

import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.modelo.Usuario;
import com.makers.sistemabancario.infraestructura.persistencia.mapper.UsuarioMapper;
import com.makers.sistemabancario.infraestructura.persistencia.repositorio.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositorioAdapter implements UsuarioRepositorioPuerto {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper mapper;

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioJpaRepository.findById(id).map(mapper::toDominio);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioJpaRepository.findByEmail(email).map(mapper::toDominio);
    }

    @Override
    public List<Usuario> buscarPorIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return usuarioJpaRepository.findAllById(ids).stream().map(mapper::toDominio).toList();
    }
}
