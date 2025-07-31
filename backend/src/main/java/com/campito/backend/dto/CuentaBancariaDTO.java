package com.campito.backend.dto;

import com.campito.backend.model.CuentaBancaria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CuentaBancariaDTO(
    Long id,
    @Size(max = 50, message = "El nombre de la cuenta no puede exceder los 50 caracteres")
    @NotBlank(message = "El nombre de la cuenta no puede estar vacío")
    String nombre,
    @Size(max = 50, message = "La entidad financiera no puede exceder los 50 caracteres")
    @NotBlank(message = "La entidad financiera no puede estar vacía")
    String entidadFinanciera,
    @NotNull(message = "El ID del espacio de trabajo no puede ser nulo")
    Long idEspacioTrabajo
) {
    public CuentaBancaria toCuentaBancaria() {
        CuentaBancaria cuentaBancaria = new CuentaBancaria();
        cuentaBancaria.setNombre(this.nombre);
        cuentaBancaria.setEntidadFinanciera(this.entidadFinanciera);
        cuentaBancaria.setSaldoActual(0f); // Asignar saldo inicial a 0
        return cuentaBancaria;
    }
}
