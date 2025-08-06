package com.campito.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campito.backend.model.MotivoTransaccion;

import java.util.List;

public interface MotivoTransaccionRepository extends JpaRepository<MotivoTransaccion, Long> {
    List<MotivoTransaccion> findByEspacioTrabajo_Id(Long idEspacioTrabajo);
}
