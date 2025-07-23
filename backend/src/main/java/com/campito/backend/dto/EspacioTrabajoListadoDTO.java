package com.campito.backend.dto;

public record EspacioTrabajoListadoDTO(
    Long id,
    String nombre,
    Float saldo,
    Long usuarioAdminId
) {

}
