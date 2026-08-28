package com.makers.sistemabancario.aplicacion.puerto.entrada;

import com.makers.sistemabancario.dominio.modelo.Prestamo;

public interface AprobarRechazarPrestamoUseCase {

    Prestamo aprobar(Long prestamoId, Long adminId);

    Prestamo rechazar(Long prestamoId, Long adminId);
}
