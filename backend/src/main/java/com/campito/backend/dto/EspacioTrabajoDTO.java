package com.campito.backend.dto;

import com.campito.backend.model.EspacioTrabajo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EspacioTrabajoDTO(
    @NotNull(message = "El nombre del espacio de trabajo no puede ser nulo")
    @Size(max = 50, message = "El nombre del espacio de trabajo no puede exceder los 50 caracteres")
    String nombre,
    Long idUsuarioAdmin
) {
    public EspacioTrabajo toEspacioTrabajo() {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();
        espacioTrabajo.setNombre(nombre);

        return espacioTrabajo;
    }
}
