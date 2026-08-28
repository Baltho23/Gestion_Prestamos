package com.makers.sistemabancario.aplicacion.servicio;

import com.makers.sistemabancario.aplicacion.puerto.entrada.AprobarRechazarPrestamoUseCase;
import com.makers.sistemabancario.aplicacion.puerto.salida.PrestamoRepositorioPuerto;
import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.excepcion.OperacionNoAutorizadaException;
import com.makers.sistemabancario.dominio.excepcion.PrestamoNoEncontradoException;
import com.makers.sistemabancario.dominio.excepcion.UsuarioNoEncontradoException;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import com.makers.sistemabancario.dominio.modelo.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AprobarRechazarPrestamoService implements AprobarRechazarPrestamoUseCase {

    private final PrestamoRepositorioPuerto prestamoRepositorioPuerto;
    private final UsuarioRepositorioPuerto usuarioRepositorioPuerto;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "prestamos-todos", allEntries = true),
            @CacheEvict(value = "prestamos-usuario", allEntries = true)
    })
    public Prestamo aprobar(Long prestamoId, Long adminId) {
        Prestamo prestamo = obtenerPrestamo(prestamoId);
        validarAdmin(adminId);
        prestamo.aprobar(adminId);
        return prestamoRepositorioPuerto.guardar(prestamo);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "prestamos-todos", allEntries = true),
            @CacheEvict(value = "prestamos-usuario", allEntries = true)
    })
    public Prestamo rechazar(Long prestamoId, Long adminId) {
        Prestamo prestamo = obtenerPrestamo(prestamoId);
        validarAdmin(adminId);
        prestamo.rechazar(adminId);
        return prestamoRepositorioPuerto.guardar(prestamo);
    }

    private Prestamo obtenerPrestamo(Long prestamoId) {
        return prestamoRepositorioPuerto.buscarPorId(prestamoId)
                .orElseThrow(() -> new PrestamoNoEncontradoException(prestamoId));
    }

    private void validarAdmin(Long adminId) {
        Usuario admin = usuarioRepositorioPuerto.buscarPorId(adminId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(adminId));
        if (!admin.esAdmin()) {
            throw new OperacionNoAutorizadaException("El usuario no tiene rol ADMIN");
        }
    }
}
