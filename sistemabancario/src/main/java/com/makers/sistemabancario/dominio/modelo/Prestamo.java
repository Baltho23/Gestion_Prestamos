package com.makers.sistemabancario.dominio.modelo;

import com.makers.sistemabancario.dominio.excepcion.OperacionNoAutorizadaException;
import com.makers.sistemabancario.dominio.excepcion.PlazoInvalidoException;
import com.makers.sistemabancario.dominio.excepcion.SaldoInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Prestamo {

    private final Long id;
    private final Long usuarioId;
    private final BigDecimal monto;
    private final int plazoMeses;
    private final LocalDateTime fechaSolicitud;
    private final int version;

    private Long resueltoPorId;
    private EstadoPrestamo estado;
    private LocalDateTime fechaResolucion;

    private Prestamo(Long id, Long usuarioId, Long resueltoPorId, BigDecimal monto, int plazoMeses,
                     EstadoPrestamo estado, LocalDateTime fechaSolicitud, LocalDateTime fechaResolucion, int version) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.resueltoPorId = resueltoPorId;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaResolucion = fechaResolucion;
        this.version = version;
    }

    public static Prestamo solicitar(Long usuarioId, BigDecimal monto, int plazoMeses) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SaldoInvalidoException("El monto debe ser mayor a cero");
        }
        if (plazoMeses <= 0) {
            throw new PlazoInvalidoException("El plazo en meses debe ser mayor a cero");
        }
        return new Prestamo(null, usuarioId, null, monto, plazoMeses,
                EstadoPrestamo.PENDIENTE, LocalDateTime.now(), null, 0);
    }

    public static Prestamo reconstruir(Long id, Long usuarioId, Long resueltoPorId, BigDecimal monto,
                                       int plazoMeses, EstadoPrestamo estado, LocalDateTime fechaSolicitud,
                                       LocalDateTime fechaResolucion, int version) {
        return new Prestamo(id, usuarioId, resueltoPorId, monto, plazoMeses,
                estado, fechaSolicitud, fechaResolucion, version);
    }

    public void aprobar(Long adminId) {
        validarQueEstePendiente();
        this.estado = EstadoPrestamo.APROBADO;
        this.resueltoPorId = adminId;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void rechazar(Long adminId) {
        validarQueEstePendiente();
        this.estado = EstadoPrestamo.RECHAZADO;
        this.resueltoPorId = adminId;
        this.fechaResolucion = LocalDateTime.now();
    }

    private void validarQueEstePendiente() {
        if (!EstadoPrestamo.PENDIENTE.equals(this.estado)) {
            throw new OperacionNoAutorizadaException(
                    "El préstamo ya fue resuelto. Estado actual: " + this.estado);
        }
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public Long getResueltoPorId() { return resueltoPorId; }
    public BigDecimal getMonto() { return monto; }
    public int getPlazoMeses() { return plazoMeses; }
    public EstadoPrestamo getEstado() { return estado; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public int getVersion() { return version; }
}
