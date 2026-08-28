package com.makers.sistemabancario.aplicacion.servicio;

import com.makers.sistemabancario.aplicacion.puerto.salida.PrestamoRepositorioPuerto;
import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.excepcion.PlazoInvalidoException;
import com.makers.sistemabancario.dominio.excepcion.SaldoInvalidoException;
import com.makers.sistemabancario.dominio.excepcion.UsuarioNoEncontradoException;
import com.makers.sistemabancario.dominio.modelo.EstadoPrestamo;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import com.makers.sistemabancario.dominio.modelo.Rol;
import com.makers.sistemabancario.dominio.modelo.Usuario;
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
class SolicitarPrestamoServiceTest {

    @Mock
    private PrestamoRepositorioPuerto prestamoRepositorioPuerto;

    @Mock
    private UsuarioRepositorioPuerto usuarioRepositorioPuerto;

    @InjectMocks
    private SolicitarPrestamoService solicitarPrestamoService;

    @Test
    void solicitar_exitoso_creaPrestamoEnEstadoPendiente() {
        Long usuarioId = 1L;
        BigDecimal monto = new BigDecimal("5000.00");
        int plazoMeses = 12;

        Usuario usuario = new Usuario(usuarioId, "usuario@test.com", "encoded", "Test User", Rol.USER, LocalDateTime.now());
        Prestamo prestamoGuardado = Prestamo.reconstruir(1L, usuarioId, null, monto, plazoMeses,
                EstadoPrestamo.PENDIENTE, LocalDateTime.now(), null, 0);

        when(usuarioRepositorioPuerto.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(prestamoRepositorioPuerto.guardar(any(Prestamo.class))).thenReturn(prestamoGuardado);

        Prestamo resultado = solicitarPrestamoService.solicitar(usuarioId, monto, plazoMeses);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo(EstadoPrestamo.PENDIENTE);
        assertThat(resultado.getMonto()).isEqualByComparingTo(monto);
        assertThat(resultado.getUsuarioId()).isEqualTo(usuarioId);
        verify(prestamoRepositorioPuerto).guardar(any(Prestamo.class));
    }

    @Test
    void solicitar_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioRepositorioPuerto.buscarPorId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitarPrestamoService.solicitar(99L, new BigDecimal("1000"), 6))
                .isInstanceOf(UsuarioNoEncontradoException.class);

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }

    @Test
    void solicitar_montoNegativo_lanzaSaldoInvalidoException() {
        Usuario usuario = new Usuario(1L, "usuario@test.com", "encoded", "Test User", Rol.USER, LocalDateTime.now());
        when(usuarioRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> solicitarPrestamoService.solicitar(1L, new BigDecimal("-500"), 12))
                .isInstanceOf(SaldoInvalidoException.class);

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }

    @Test
    void solicitar_montoCero_lanzaSaldoInvalidoException() {
        Usuario usuario = new Usuario(1L, "usuario@test.com", "encoded", "Test User", Rol.USER, LocalDateTime.now());
        when(usuarioRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> solicitarPrestamoService.solicitar(1L, BigDecimal.ZERO, 12))
                .isInstanceOf(SaldoInvalidoException.class);

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }

    @Test
    void solicitar_plazoNegativo_lanzaExcepcion() {
        Usuario usuario = new Usuario(1L, "usuario@test.com", "encoded", "Test User", Rol.USER, LocalDateTime.now());
        when(usuarioRepositorioPuerto.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> solicitarPrestamoService.solicitar(1L, new BigDecimal("1000"), -1))
                .isInstanceOf(PlazoInvalidoException.class);

        verify(prestamoRepositorioPuerto, never()).guardar(any());
    }
}
