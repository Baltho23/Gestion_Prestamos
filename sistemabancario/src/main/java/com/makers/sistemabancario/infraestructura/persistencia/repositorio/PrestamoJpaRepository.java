package com.makers.sistemabancario.infraestructura.persistencia.repositorio;

import com.makers.sistemabancario.infraestructura.persistencia.entidad.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoJpaRepository extends JpaRepository<PrestamoEntity, Long> {

    List<PrestamoEntity> findByUsuarioId(Long usuarioId);
}
