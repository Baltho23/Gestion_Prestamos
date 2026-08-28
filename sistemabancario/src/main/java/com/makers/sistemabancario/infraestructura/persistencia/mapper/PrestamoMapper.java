package com.makers.sistemabancario.infraestructura.persistencia.mapper;

import com.makers.sistemabancario.dominio.modelo.Prestamo;
import com.makers.sistemabancario.infraestructura.persistencia.entidad.PrestamoEntity;
import com.makers.sistemabancario.infraestructura.persistencia.entidad.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class PrestamoMapper {

    public Prestamo toDominio(PrestamoEntity entity) {
        return Prestamo.reconstruir(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getResueltoPor() != null ? entity.getResueltoPor().getId() : null,
                entity.getMonto(),
                entity.getPlazoMeses(),
                entity.getEstado(),
                entity.getFechaSolicitud(),
                entity.getFechaResolucion(),
                entity.getVersion()
        );
    }

    public PrestamoEntity toEntity(Prestamo dominio, UsuarioEntity usuario, UsuarioEntity resueltoPor) {
        return PrestamoEntity.builder()
                .id(dominio.getId())
                .usuario(usuario)
                .resueltoPor(resueltoPor)
                .monto(dominio.getMonto())
                .plazoMeses(dominio.getPlazoMeses())
                .estado(dominio.getEstado())
                .fechaSolicitud(dominio.getFechaSolicitud())
                .fechaResolucion(dominio.getFechaResolucion())
                .version(dominio.getVersion())
                .build();
    }
}
