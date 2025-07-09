package com.campito.backend.service;

import com.campito.backend.dto.EspacioTrabajoDTO;

public interface EspacioTrabajoService {
    public void registrarEspacioTrabajo(EspacioTrabajoDTO espacioTrabajoDTO);
    public void compartirEspacioTrabajo(Long idUsuario, Long idEspacioTrabajo, Long idUsuarioAdmin);
}
