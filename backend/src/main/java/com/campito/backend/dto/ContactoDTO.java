package com.campito.backend.dto;

import com.campito.backend.model.ContactoTransferencia;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactoDTO(
    @Size(max = 50, message = "El nombre de contacto no puede exceder los 50 caracteres")
    String nombre,
    @NotNull(message = "El ID del espacio de trabajo no puede ser nulo")
    Long idEspacioTrabajo
) {
    public ContactoTransferencia toContactoTransferencia() {
        ContactoTransferencia contactoTransferencia = new ContactoTransferencia();
        contactoTransferencia.setNombre(this.nombre);
        return contactoTransferencia;
    }
}
