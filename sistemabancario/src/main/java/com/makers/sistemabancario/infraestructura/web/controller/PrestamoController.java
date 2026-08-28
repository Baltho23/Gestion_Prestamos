package com.makers.sistemabancario.infraestructura.web.controller;

import com.makers.sistemabancario.aplicacion.puerto.entrada.AprobarRechazarPrestamoUseCase;
import com.makers.sistemabancario.aplicacion.puerto.entrada.ConsultarPrestamosUseCase;
import com.makers.sistemabancario.aplicacion.puerto.entrada.SolicitarPrestamoUseCase;
import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.excepcion.UsuarioNoEncontradoException;
import com.makers.sistemabancario.dominio.modelo.Prestamo;
import com.makers.sistemabancario.dominio.modelo.Usuario;
import com.makers.sistemabancario.infraestructura.web.dto.request.SolicitarPrestamoRequest;
import com.makers.sistemabancario.infraestructura.web.dto.response.PrestamoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final SolicitarPrestamoUseCase solicitarPrestamoUseCase;
    private final AprobarRechazarPrestamoUseCase aprobarRechazarPrestamoUseCase;
    private final ConsultarPrestamosUseCase consultarPrestamosUseCase;
    private final UsuarioRepositorioPuerto usuarioRepositorioPuerto;

    @PostMapping
    public ResponseEntity<PrestamoResponse> solicitar(
            @Valid @RequestBody SolicitarPrestamoRequest request,
            Authentication authentication) {
        Long usuarioId = obtenerUsuarioId(authentication);
        Prestamo prestamo = solicitarPrestamoUseCase.solicitar(usuarioId, request.monto(), request.plazoMeses());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(prestamo));
    }

    @GetMapping("/mis")
    public ResponseEntity<List<PrestamoResponse>> misPrestamos(Authentication authentication) {
        Long usuarioId = obtenerUsuarioId(authentication);
        return ResponseEntity.ok(toResponses(consultarPrestamosUseCase.consultarPorUsuario(usuarioId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestamoResponse>> todos() {
        return ResponseEntity.ok(toResponses(consultarPrestamosUseCase.consultarTodos()));
    }

    @PatchMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestamoResponse> aprobar(@PathVariable Long id, Authentication authentication) {
        Long adminId = obtenerUsuarioId(authentication);
        return ResponseEntity.ok(toResponse(aprobarRechazarPrestamoUseCase.aprobar(id, adminId)));
    }

    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestamoResponse> rechazar(@PathVariable Long id, Authentication authentication) {
        Long adminId = obtenerUsuarioId(authentication);
        return ResponseEntity.ok(toResponse(aprobarRechazarPrestamoUseCase.rechazar(id, adminId)));
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        return usuarioRepositorioPuerto.buscarPorEmail(authentication.getName())
                .orElseThrow(() -> new UsuarioNoEncontradoException(authentication.getName()))
                .getId();
    }

    private PrestamoResponse toResponse(Prestamo prestamo) {
        Set<Long> ids = new HashSet<>();
        ids.add(prestamo.getUsuarioId());
        if (prestamo.getResueltoPorId() != null) {
            ids.add(prestamo.getResueltoPorId());
        }
        return toResponse(prestamo, nombresPorId(ids));
    }

    /** Resuelve los nombres (solicitante y quien resolvió) en una sola consulta — evita N+1 al listar. */
    private List<PrestamoResponse> toResponses(List<Prestamo> prestamos) {
        Set<Long> ids = new HashSet<>();
        for (Prestamo p : prestamos) {
            ids.add(p.getUsuarioId());
            if (p.getResueltoPorId() != null) {
                ids.add(p.getResueltoPorId());
            }
        }
        Map<Long, String> nombrePorId = nombresPorId(ids);
        return prestamos.stream()
                .map(p -> toResponse(p, nombrePorId))
                .toList();
    }

    private Map<Long, String> nombresPorId(Set<Long> ids) {
        return usuarioRepositorioPuerto.buscarPorIds(ids).stream()
                .collect(Collectors.toMap(Usuario::getId, Usuario::getNombre));
    }

    private PrestamoResponse toResponse(Prestamo prestamo, Map<Long, String> nombrePorId) {
        Long resueltoPorId = prestamo.getResueltoPorId();
        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getUsuarioId(),
                nombrePorId.get(prestamo.getUsuarioId()),
                resueltoPorId,
                resueltoPorId != null ? nombrePorId.get(resueltoPorId) : null,
                prestamo.getMonto(),
                prestamo.getPlazoMeses(),
                prestamo.getEstado().name(),
                prestamo.getFechaSolicitud(),
                prestamo.getFechaResolucion()
        );
    }
}
