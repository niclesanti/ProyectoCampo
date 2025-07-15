package com.campito.backend.service;

import java.util.List;

import com.campito.backend.dto.ContactoDTO;
import com.campito.backend.dto.ContactoListadoDTO;
import com.campito.backend.dto.MotivoDTO;
import com.campito.backend.dto.MotivoListadoDTO;
import com.campito.backend.dto.TransaccionBusquedaDTO;
import com.campito.backend.dto.TransaccionDTO;
import com.campito.backend.dto.TransaccionListadoDTO;

public interface TransaccionService {
    public void registrarTransaccion(TransaccionDTO transaccionDTO);
    public void removerTransaccion(Long id);
    public List<TransaccionListadoDTO> buscarTransaccion(TransaccionBusquedaDTO datosBusqueda);
    public void registrarContactoTransferencia(ContactoDTO contactoDTO);
    public MotivoDTO nuevoMotivoTransaccion(MotivoDTO motivoDTO);
    public List<ContactoListadoDTO> listarContactos(Long idEspacioTrabajo);
    public List<MotivoListadoDTO> listarMotivos(Long idEspacioTrabajo);
}
