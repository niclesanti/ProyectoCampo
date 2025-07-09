package com.campito.backend.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dao.ContactoTransferenciaRepository;
import com.campito.backend.dao.EspacioTrabajoRepository;
import com.campito.backend.dao.MotivoTransaccionRepository;
import com.campito.backend.dao.TransaccionRepository;
import com.campito.backend.dto.ContactoDTO;
import com.campito.backend.dto.ContactoListadoDTO;
import com.campito.backend.dto.MotivoDTO;
import com.campito.backend.dto.MotivoListadoDTO;
import com.campito.backend.dto.TransaccionBusquedaDTO;
import com.campito.backend.dto.TransaccionDTO;
import com.campito.backend.dto.TransaccionListadoDTO;
import com.campito.backend.model.ContactoTransferencia;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.MotivoTransaccion;
import com.campito.backend.model.TipoTransaccion;
import com.campito.backend.model.Transaccion;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final EspacioTrabajoRepository espacioRepository;
    private final MotivoTransaccionRepository motivoRepository;
    private final ContactoTransferenciaRepository contactoRepository;

    @Autowired
    public TransaccionServiceImpl(
        TransaccionRepository transaccionRepository,
        EspacioTrabajoRepository espacioRepository,
        MotivoTransaccionRepository motivoRepository,
        ContactoTransferenciaRepository contactoRepository) {
        this.transaccionRepository = transaccionRepository;
        this.espacioRepository = espacioRepository;
        this.motivoRepository = motivoRepository;
        this.contactoRepository = contactoRepository;
    }

    @Override
    @Transactional
    public void registrarTransaccion(TransaccionDTO transaccionDTO) {
        if(transaccionDTO == null) {
            throw new IllegalArgumentException("La transaccion no puede ser nula");
        }
        if(transaccionDTO.idEspacioTrabajo() == null) {
            throw new IllegalArgumentException("El espacio de trabajo de la transaccion no puede ser nulo");
        }
        if(transaccionDTO.idMotivo() == null) {
            throw new IllegalArgumentException("El motivo de la transaccion no puede ser nulo");
        }

        Transaccion transaccion = transaccionDTO.toTransaccion();

        // Agregar fecha de creacion actual en la zona horaria de Buenos Aires (GMT-3)
        ZoneId buenosAiresZone = ZoneId.of("America/Argentina/Buenos_Aires");
        ZonedDateTime nowInBuenosAires = ZonedDateTime.now(buenosAiresZone);
        transaccion.setFechaCreacion(nowInBuenosAires.toLocalDateTime());

        EspacioTrabajo espacio = espacioRepository.findById(transaccionDTO.idEspacioTrabajo()).orElseThrow(() -> new EntityNotFoundException("Espacio de trabajo con ID " + transaccionDTO.idEspacioTrabajo() + " no encontrado"));

        MotivoTransaccion motivo = motivoRepository.findById(transaccionDTO.idMotivo()).orElseThrow(() -> new EntityNotFoundException("Motivo de transacción con ID " + transaccionDTO.idMotivo() + " no encontrado"));

        // Actualizar saldo en el espacio de trabajo
        if(transaccion.getTipo().equals(TipoTransaccion.INGRESO)) {
            espacio.setSaldo(espacio.getSaldo() + transaccion.getMonto());
        } 
        else {
            espacio.setSaldo(espacio.getSaldo() - transaccion.getMonto());
        }
        espacioRepository.save(espacio);
        
        // Asignar espacio de trabajo y motivo a la transacción
        transaccion.setEspacioTrabajo(espacio);
        transaccion.setMotivo(motivo);

        // Si se proporciona un ID de contacto, buscar el contacto y asignarlo a la transacción
        if(transaccionDTO.idContacto() != null) {
            ContactoTransferencia contacto = contactoRepository.findById(transaccionDTO.idContacto()).orElseThrow(() -> new EntityNotFoundException("Contacto de transferencia con ID " + transaccionDTO.idContacto() + " no encontrado"));
            transaccion.setContacto(contacto);
        }

        transaccionRepository.save(transaccion);

    }

    @Override
    @Transactional
    public void removerTransaccion(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la transacción no puede ser nulo");
        }

        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transacción con ID " + id + " no encontrada"));

        // Actualizar el saldo del espacio de trabajo asociado

        EspacioTrabajo espacio = transaccion.getEspacioTrabajo();
        if (transaccion.getTipo().equals(TipoTransaccion.INGRESO)) {
            espacio.setSaldo(espacio.getSaldo() - transaccion.getMonto());
        } else {
            espacio.setSaldo(espacio.getSaldo() + transaccion.getMonto());
        }

        transaccionRepository.delete(transaccion);

        espacioRepository.save(espacio);
    }

    @Override
    public List<TransaccionListadoDTO> buscarTransaccion(TransaccionBusquedaDTO datosBusqueda) {
        if (datosBusqueda == null) {
            throw new IllegalArgumentException("Los datos de búsqueda no pueden ser nulos");
        }
        if (datosBusqueda.idEspacioTrabajo() == null) {
            throw new IllegalArgumentException("El ID del espacio de trabajo no puede ser nulo");
        }

        Specification<Transaccion> spec = (root, query, cb) -> cb.equal(root.get("espacioTrabajo").get("id"), datosBusqueda.idEspacioTrabajo());

        // Filtrado por fecha usando rango
        if (datosBusqueda.anio() != null) {
            int anio = datosBusqueda.anio();
            int mes = datosBusqueda.mes() != null ? datosBusqueda.mes() : 1;
            java.time.LocalDate desde = java.time.LocalDate.of(anio, mes, 1);
            java.time.LocalDate hasta;
            if (datosBusqueda.mes() != null) {
                // Último día del mes
                hasta = desde.withDayOfMonth(desde.lengthOfMonth());
            } else {
                // Último día del año
                hasta = java.time.LocalDate.of(anio, 12, 31);
            }
            spec = spec.and((root, query, cb) -> cb.between(root.get("fecha"), desde, hasta));
        }

        if (datosBusqueda.motivo() != null && !datosBusqueda.motivo().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("motivo").get("motivo")), "%" + datosBusqueda.motivo().toLowerCase() + "%"));
        }
        if (datosBusqueda.contacto() != null && !datosBusqueda.contacto().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("contacto").get("nombre")), "%" + datosBusqueda.contacto().toLowerCase() + "%"));
        }

        List<Transaccion> transacciones = transaccionRepository.findAll(spec);
        return crearListadoTransacciones(transacciones);
    }

    @Override
    @Transactional
    public void registrarContactoTransferencia(ContactoDTO contactoDTO) {
        if(contactoDTO == null || contactoDTO.nombre() == null || contactoDTO.nombre().isEmpty()) {
            throw new IllegalArgumentException("El contacto no puede ser nulo");
        }

        ContactoTransferencia contacto = contactoDTO.toContactoTransferencia();

        EspacioTrabajo espacio = espacioRepository.findById(contactoDTO.idEspacioTrabajo()).orElseThrow(() -> new EntityNotFoundException("Espacio de trabajo con ID " + contactoDTO.idEspacioTrabajo() + " no encontrado"));
        contacto.setEspacioTrabajo(espacio);

        contactoRepository.save(contacto);
    }

    @Override
    @Transactional
    public void nuevoMotivoTransaccion(MotivoDTO motivoDTO) {
        if(motivoDTO == null || motivoDTO.motivo() == null || motivoDTO.motivo().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede ser nulo");
        }

        MotivoTransaccion motivo = motivoDTO.toMotivoTransaccion();

        EspacioTrabajo espacio = espacioRepository.findById(motivoDTO.idEspacioTrabajo()).orElseThrow(() -> new EntityNotFoundException("Espacio de trabajo con ID " + motivoDTO.idEspacioTrabajo() + " no encontrado"));
        motivo.setEspacioTrabajo(espacio);

        motivoRepository.save(motivo);
    }

    @Override
    public List<ContactoListadoDTO> listarContactos(Long idEspacioTrabajo) {
        if(idEspacioTrabajo == null) {
            throw new IllegalArgumentException("El id del espacio de trabajo no puede ser nulo");
        }

        return contactoRepository.findByEspacioTrabajo_Id(idEspacioTrabajo).stream()
                .map(contacto -> new ContactoListadoDTO(contacto.getId(), contacto.getNombre()))
                .toList();
    }

    @Override
    public List<MotivoListadoDTO> listarMotivos(Long idEspacioTrabajo) {
        if(idEspacioTrabajo == null) {
            throw new IllegalArgumentException("El id del espacio de trabajo no puede ser nulo");
        }

        return motivoRepository.findByEspacioTrabajo_Id(idEspacioTrabajo).stream()
                .map(motivo -> new MotivoListadoDTO(motivo.getId(), motivo.getMotivo()))
                .toList();
    }

    // Metodos auxiliares

    private List<TransaccionListadoDTO> crearListadoTransacciones(List<Transaccion> transacciones) {
        return transacciones.stream()
                .map(t -> new TransaccionListadoDTO(
                    t.getId(),
                    t.getFecha(),
                    t.getMonto(),
                    t.getTipo(),
                    t.getDescripcion(),
                    t.getNombreCompletoAuditoria(),
                    t.getFechaCreacion(),
                    t.getEspacioTrabajo() != null ? t.getEspacioTrabajo().getId() : null,
                    t.getEspacioTrabajo() != null ? t.getEspacioTrabajo().getNombre() : null,
                    t.getMotivo() != null ? t.getMotivo().getId() : null,
                    t.getMotivo() != null ? t.getMotivo().getMotivo() : null,
                    t.getContacto() != null ? t.getContacto().getId() : null,
                    t.getContacto() != null ? t.getContacto().getNombre() : null
                ))
                .toList();
    }
    
}
