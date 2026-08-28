package com.makers.sistemabancario.infraestructura.persistencia.adaptador;

import com.makers.sistemabancario.aplicacion.puerto.salida.PrestamoRepositorioPuerto;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import com.makers.sistemabancario.infraestructura.persistencia.entidad.UsuarioEntity;
import com.makers.sistemabancario.infraestructura.persistencia.mapper.PrestamoMapper;
import com.makers.sistemabancario.infraestructura.persistencia.repositorio.PrestamoJpaRepository;
import com.makers.sistemabancario.infraestructura.persistencia.repositorio.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PrestamoRepositorioAdapter implements PrestamoRepositorioPuerto {

    private final PrestamoJpaRepository prestamoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PrestamoMapper mapper;

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        UsuarioEntity usuario = usuarioJpaRepository.findById(prestamo.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + prestamo.getUsuarioId()));

        UsuarioEntity resueltoPor = null;
        if (prestamo.getResueltoPorId() != null) {
            resueltoPor = usuarioJpaRepository.findById(prestamo.getResueltoPorId())
                    .orElseThrow(() -> new RuntimeException("Admin no encontrado: " + prestamo.getResueltoPorId()));
        }

        return mapper.toDominio(prestamoJpaRepository.save(mapper.toEntity(prestamo, usuario, resueltoPor)));
    }

    @Override
    public Optional<Prestamo> buscarPorId(Long id) {
        return prestamoJpaRepository.findById(id).map(mapper::toDominio);
    }

    @Override
    public List<Prestamo> buscarPorUsuarioId(Long usuarioId) {
        return prestamoJpaRepository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDominio)
                .toList();
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return prestamoJpaRepository.findAll().stream()
                .map(mapper::toDominio)
                .toList();
    }
}
