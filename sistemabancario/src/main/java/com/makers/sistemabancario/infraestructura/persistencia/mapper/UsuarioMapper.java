package com.makers.sistemabancario.infraestructura.persistencia.mapper;

import com.makers.sistemabancario.dominio.modelo.Usuario;
import com.makers.sistemabancario.infraestructura.persistencia.entidad.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDominio(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getNombre(),
                entity.getRol(),
                entity.getFechaCreacion()
        );
    }
}
