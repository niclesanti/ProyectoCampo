package com.campito.backend.service;

import java.util.List;

import com.campito.backend.dto.CuentaBancariaDTO;
import com.campito.backend.dto.CuentaBancariaListadoDTO;
import com.campito.backend.model.CuentaBancaria;
import com.campito.backend.model.TipoTransaccion;

public interface CuentaBancariaService {
    public void crearCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO);
    public CuentaBancaria actualizarCuentaBancaria(Long id, TipoTransaccion tipo, Float monto);
    public List<CuentaBancariaListadoDTO> listarCuentasBancarias(Long idEspacioTrabajo);
    public void transaccionEntreCuentas(Long idCuentaOrigen, Long idCuentaDestino, Float monto);
}
