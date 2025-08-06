package com.campito.backend.dao;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.campito.backend.model.EspacioTrabajo;

public interface EspacioTrabajoRepository extends JpaRepository<EspacioTrabajo, Long> {
    // Busca todos los espacios de trabajo donde participa un usuario por su id
    List<EspacioTrabajo> findByUsuariosParticipantes_Id(Long idUsuario);
}
