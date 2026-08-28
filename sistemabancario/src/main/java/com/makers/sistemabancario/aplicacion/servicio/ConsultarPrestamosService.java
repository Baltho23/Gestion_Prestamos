package com.makers.sistemabancario.aplicacion.servicio;

import com.makers.sistemabancario.aplicacion.puerto.entrada.ConsultarPrestamosUseCase;
import com.makers.sistemabancario.aplicacion.puerto.salida.PrestamoRepositorioPuerto;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarPrestamosService implements ConsultarPrestamosUseCase {

    private final PrestamoRepositorioPuerto prestamoRepositorioPuerto;

    @Override
    @Cacheable(value = "prestamos-usuario", key = "#usuarioId")
    @Transactional(readOnly = true)
    public List<Prestamo> consultarPorUsuario(Long usuarioId) {
        return prestamoRepositorioPuerto.buscarPorUsuarioId(usuarioId);
    }

    @Override
    @Cacheable(value = "prestamos-todos")
    @Transactional(readOnly = true)
    public List<Prestamo> consultarTodos() {
        return prestamoRepositorioPuerto.buscarTodos();
    }
}
