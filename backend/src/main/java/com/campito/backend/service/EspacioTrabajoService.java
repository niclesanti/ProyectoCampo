package com.campito.backend.service;

import java.util.List;

import com.campito.backend.dto.EspacioTrabajoDTO;
import com.campito.backend.dto.EspacioTrabajoListadoDTO;

public interface EspacioTrabajoService {
    public void registrarEspacioTrabajo(EspacioTrabajoDTO espacioTrabajoDTO);
    public void compartirEspacioTrabajo(Long idUsuario, Long idEspacioTrabajo, Long idUsuarioAdmin);
    public List<EspacioTrabajoListadoDTO> listarEspaciosTrabajoPorUsuario(Long idUsuario);
}
