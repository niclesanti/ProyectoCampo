package com.campito.backend.dto;

import java.util.List;

public record DashboardInfoDTO(
    List<IngresosGastosMesDTO> ingresosGastos,
    List<DistribucionGastoDTO> distribucionGastos,
    List<SaldoAcumuladoMesDTO> saldoAcumuladoMes
) {

}
