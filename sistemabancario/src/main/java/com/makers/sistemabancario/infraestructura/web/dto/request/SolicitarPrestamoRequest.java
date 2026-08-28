package com.makers.sistemabancario.infraestructura.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SolicitarPrestamoRequest(
        @NotNull @Positive BigDecimal monto,
        @NotNull @Positive Integer plazoMeses
) {}
