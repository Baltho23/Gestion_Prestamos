package com.makers.sistemabancario.aplicacion.puerto.entrada;

import com.makers.sistemabancario.dominio.modelo.Prestamo;

import java.math.BigDecimal;

public interface SolicitarPrestamoUseCase {

    Prestamo solicitar(Long usuarioId, BigDecimal monto, int plazoMeses);
}
