package com.makers.sistemabancario.aplicacion.servicio;

import com.makers.sistemabancario.aplicacion.puerto.entrada.SolicitarPrestamoUseCase;
import com.makers.sistemabancario.aplicacion.puerto.salida.PrestamoRepositorioPuerto;
import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.excepcion.UsuarioNoEncontradoException;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SolicitarPrestamoService implements SolicitarPrestamoUseCase {

    private final PrestamoRepositorioPuerto prestamoRepositorioPuerto;
    private final UsuarioRepositorioPuerto usuarioRepositorioPuerto;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "prestamos-todos", allEntries = true),
            @CacheEvict(value = "prestamos-usuario", allEntries = true)
    })
    public Prestamo solicitar(Long usuarioId, BigDecimal monto, int plazoMeses) {
        usuarioRepositorioPuerto.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        Prestamo prestamo = Prestamo.solicitar(usuarioId, monto, plazoMeses);
        return prestamoRepositorioPuerto.guardar(prestamo);
    }
}
