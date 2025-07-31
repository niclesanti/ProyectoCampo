package com.campito.backend.service;

import java.util.List;

import com.campito.backend.dto.CuentaBancariaDTO;
import com.campito.backend.dto.CuentaBancariaListadoDTO;

public interface CuentaBancariaService {
    public void crearCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO);
    public void actualizarCuentaBancaria(Long id, Float monto);
    public List<CuentaBancariaListadoDTO> listarCuentasBancarias(Long idEspacioTrabajo);
    public void transaccionEntreCuentas(Long idCuentaOrigen, Long idCuentaDestino, Float monto);
}
