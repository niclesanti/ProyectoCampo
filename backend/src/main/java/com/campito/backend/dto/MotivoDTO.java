package com.campito.backend.dto;

import com.campito.backend.model.MotivoTransaccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MotivoDTO(
    Long id,
    @Size(max = 50, message = "El motivo no puede exceder los 50 caracteres")
    @NotBlank(message = "El motivo no puede estar vacío")
    String motivo,
    @NotNull(message = "El ID del espacio de trabajo no puede ser nulo")
    Long idEspacioTrabajo
) {
    public MotivoTransaccion toMotivoTransaccion() {
        MotivoTransaccion motivo = new MotivoTransaccion();
        motivo.setMotivo(this.motivo);
        return motivo;
    }
}
