package com.makers.sistemabancario.infraestructura.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PrestamoResponse(
        Long id,
        Long usuarioId,
        String solicitanteNombre,
        Long resueltoPorId,
        String resueltoPorNombre,
        BigDecimal monto,
        int plazoMeses,
        String estado,
        LocalDateTime fechaSolicitud,
        LocalDateTime fechaResolucion
) {}
