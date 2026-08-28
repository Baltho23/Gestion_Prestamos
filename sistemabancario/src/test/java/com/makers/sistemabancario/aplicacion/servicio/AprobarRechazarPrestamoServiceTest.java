package com.makers.sistemabancario.aplicacion.servicio;

import com.makers.sistemabancario.aplicacion.puerto.salida.PrestamoRepositorioPuerto;
import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.excepcion.OperacionNoAutorizadaException;
import com.makers.sistemabancario.dominio.excepcion.PrestamoNoEncontradoException;
import com.makers.sistemabancario.dominio.excepcion.UsuarioNoEncontradoException;
import com.makers.sistemabancario.dominio.modelo.EstadoPrestamo;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import com.makers.sistemabancario.dominio.modelo.Rol;
import com.makers.sistemabancario.dominio.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprobarRechazarPrestamoServiceTest {

    @Mock
    private PrestamoRepositorioPuerto prestamoRepositorioPuerto;

    @Mock
    private UsuarioRepositorioPuerto usuarioRepositorioPuerto;

    @InjectMocks
    private AprobarRechazarPrestamoService aprobarRechazarPrestamoService;

    private Usuario admin;
    private Prestamo prestamoPendiente;

    @BeforeEach
    void setUp() {
        admin = new Usuario(2L, "admin@test.com", "encoded", "Admin", Rol.ADMIN, LocalDateTime.now());
        prestamoPendiente = Prestamo.reconstruir(1L, 1L, null, new BigDecimal("5000.00"), 12,
                EstadoPrestamo.PENDIENTE, LocalDateTime.now(), null, 0);
    }

    @Test
    void aprobar_exitoso_cambiaEstadoAAprobado() {
        when(prestamoRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(prestamoPendiente));
        when(usuarioRepositorioPuerto.buscarPorId(2L)).thenReturn(Optional.of(admin));
        when(prestamoRepositorioPuerto.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        Prestamo resultado = aprobarRechazarPrestamoService.aprobar(1L, 2L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPrestamo.APROBADO);
        assertThat(resultado.getResueltoPorId()).isEqualTo(2L);
        assertThat(resultado.getFechaResolucion()).isNotNull();
        verify(prestamoRepositorioPuerto).guardar(any());
    }

    @Test
    void rechazar_exitoso_cambiaEstadoARechazado() {
        when(prestamoRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(prestamoPendiente));
        when(usuarioRepositorioPuerto.buscarPorId(2L)).thenReturn(Optional.of(admin));
        when(prestamoRepositorioPuerto.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        Prestamo resultado = aprobarRechazarPrestamoService.rechazar(1L, 2L);

        assertThat(resultado.getEstado()).isEqualTo(EstadoPrestamo.RECHAZADO);
        assertThat(resultado.getResueltoPorId()).isEqualTo(2L);
        assertThat(resultado.getFechaResolucion()).isNotNull();
    }

    @Test
    void aprobar_prestamoNoEncontrado_lanzaExcepcion() {
        when(prestamoRepositorioPuerto.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aprobarRechazarPrestamoService.aprobar(99L, 2L))
                .isInstanceOf(PrestamoNoEncontradoException.class);

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }

    @Test
    void aprobar_prestamoYaAprobado_lanzaExcepcion() {
        Prestamo prestamoAprobado = Prestamo.reconstruir(1L, 1L, 2L, new BigDecimal("5000.00"), 12,
                EstadoPrestamo.APROBADO, LocalDateTime.now(), LocalDateTime.now(), 1);

        when(prestamoRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(prestamoAprobado));
        when(usuarioRepositorioPuerto.buscarPorId(2L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> aprobarRechazarPrestamoService.aprobar(1L, 2L))
                .isInstanceOf(OperacionNoAutorizadaException.class)
                .hasMessageContaining("APROBADO");

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }

    @Test
    void rechazar_prestamoYaRechazado_lanzaExcepcion() {
        Prestamo prestamoRechazado = Prestamo.reconstruir(1L, 1L, 2L, new BigDecimal("5000.00"), 12,
                EstadoPrestamo.RECHAZADO, LocalDateTime.now(), LocalDateTime.now(), 1);

        when(prestamoRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(prestamoRechazado));
        when(usuarioRepositorioPuerto.buscarPorId(2L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> aprobarRechazarPrestamoService.rechazar(1L, 2L))
                .isInstanceOf(OperacionNoAutorizadaException.class)
                .hasMessageContaining("RECHAZADO");
    }

    @Test
    void aprobar_usuarioNoEsAdmin_lanzaExcepcion() {
        Usuario usuarioNormal = new Usuario(3L, "user@test.com", "encoded", "User", Rol.USER, LocalDateTime.now());

        when(prestamoRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(prestamoPendiente));
        when(usuarioRepositorioPuerto.buscarPorId(3L)).thenReturn(Optional.of(usuarioNormal));

        assertThatThrownBy(() -> aprobarRechazarPrestamoService.aprobar(1L, 3L))
                .isInstanceOf(OperacionNoAutorizadaException.class)
                .hasMessageContaining("ADMIN");

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }

    @Test
    void aprobar_adminNoEncontrado_lanzaExcepcion() {
        when(prestamoRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(prestamoPendiente));
        when(usuarioRepositorioPuerto.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aprobarRechazarPrestamoService.aprobar(1L, 99L))
                .isInstanceOf(UsuarioNoEncontradoException.class);
    }
}
