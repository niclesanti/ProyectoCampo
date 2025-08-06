package com.campito.backend.dto;

public record CuentaBancariaListadoDTO(
    Long id,
    String nombre,
    String entidadFinanciera,
    Float saldoActual
) {

}
