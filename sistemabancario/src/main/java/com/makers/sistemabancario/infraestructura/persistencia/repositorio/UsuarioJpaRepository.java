package com.makers.sistemabancario.infraestructura.persistencia.repositorio;

import com.makers.sistemabancario.infraestructura.persistencia.entidad.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);
}
