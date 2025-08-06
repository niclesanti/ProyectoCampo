package com.campito.backend.dto;

import java.math.BigDecimal;

public interface IngresosGastosMesDTO {
    String getMes();
    BigDecimal getIngresos();
    BigDecimal getGastos();
}