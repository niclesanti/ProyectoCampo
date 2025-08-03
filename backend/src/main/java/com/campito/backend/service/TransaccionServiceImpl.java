package com.campito.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dao.ContactoTransferenciaRepository;
import com.campito.backend.dao.CuentaBancariaRepository;
import com.campito.backend.dao.DashboardRepository;
import com.campito.backend.dao.EspacioTrabajoRepository;
import com.campito.backend.dao.MotivoTransaccionRepository;
import com.campito.backend.dao.TransaccionRepository;
import com.campito.backend.dto.ContactoDTO;
import com.campito.backend.dto.ContactoListadoDTO;
import com.campito.backend.dto.DashboardInfoDTO;
import com.campito.backend.dto.DistribucionGastoDTO;
import com.campito.backend.dto.IngresosGastosMesDTO;
import com.campito.backend.dto.MotivoDTO;
import com.campito.backend.dto.MotivoListadoDTO;
import com.campito.backend.dto.SaldoAcumuladoMesDTO;
import com.campito.backend.dto.TransaccionBusquedaDTO;
import com.campito.backend.dto.TransaccionDTO;
import com.campito.backend.dto.TransaccionListadoDTO;
import com.campito.backend.model.ContactoTransferencia;
import com.campito.backend.model.CuentaBancaria;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.MotivoTransaccion;
import com.campito.backend.model.TipoTransaccion;
import com.campito.backend.model.Transaccion;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private static final Logger logger = LoggerFactory.getLogger(TransaccionServiceImpl.class);

    private final TransaccionRepository transaccionRepository;
    private final EspacioTrabajoRepository espacioRepository;
    private final MotivoTransaccionRepository motivoRepository;
    private final ContactoTransferenciaRepository contactoRepository;
    private final DashboardRepository dashboardRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final CuentaBancariaService cuentaBancariaService;

    @Autowired
    public TransaccionServiceImpl(
        TransaccionRepository transaccionRepository,
        EspacioTrabajoRepository espacioRepository,
        MotivoTransaccionRepository motivoRepository,
        ContactoTransferenciaRepository contactoRepository,
        DashboardRepository dashboardRepository,
        CuentaBancariaRepository cuentaBancariaRepository,
        CuentaBancariaService cuentaBancariaService) {
        this.transaccionRepository = transaccionRepository;
        this.espacioRepository = espacioRepository;
        this.motivoRepository = motivoRepository;
        this.contactoRepository = contactoRepository;
        this.dashboardRepository = dashboardRepository;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @Override
    @Transactional
    public TransaccionDTO registrarTransaccion(TransaccionDTO transaccionDTO) {
        if (transaccionDTO == null) {
            logger.warn("Intento de registrar una TransaccionDTO nula.");
            throw new IllegalArgumentException("La transaccion no puede ser nula");
        }
        logger.info("Iniciando registro de transaccion tipo {} por monto {} en espacio ID {}", transaccionDTO.tipo(), transaccionDTO.monto(), transaccionDTO.idEspacioTrabajo());
        try {
            if (transaccionDTO.idEspacioTrabajo() == null) {
                logger.warn("ID de espacio de trabajo nulo al registrar transaccion.");
                throw new IllegalArgumentException("El espacio de trabajo de la transaccion no puede ser nulo");
            }
            if (transaccionDTO.idMotivo() == null) {
                logger.warn("ID de motivo nulo al registrar transaccion.");
                throw new IllegalArgumentException("El motivo de la transaccion no puede ser nulo");
            }

            EspacioTrabajo espacio = espacioRepository.findById(transaccionDTO.idEspacioTrabajo()).orElseThrow(() -> {
                String msg = "Espacio de trabajo con ID " + transaccionDTO.idEspacioTrabajo() + " no encontrado";
                logger.warn(msg);
                return new EntityNotFoundException(msg);
            });
            MotivoTransaccion motivo = motivoRepository.findById(transaccionDTO.idMotivo()).orElseThrow(() -> {
                String msg = "Motivo de transaccion con ID " + transaccionDTO.idMotivo() + " no encontrado";
                logger.warn(msg);
                return new EntityNotFoundException(msg);
            });

            Transaccion transaccion = transaccionDTO.toTransaccion();

            if (transaccionDTO.idContacto() != null) {
                ContactoTransferencia contacto = contactoRepository.findById(transaccionDTO.idContacto()).orElseThrow(() -> {
                    String msg = "Contacto de transferencia con ID " + transaccionDTO.idContacto() + " no encontrado";
                    logger.warn(msg);
                    return new EntityNotFoundException(msg);
                });
                transaccion.setContacto(contacto);
            }

            if(transaccionDTO.idCuentaBancaria() != null) {
                CuentaBancaria cuenta = cuentaBancariaService.actualizarCuentaBancaria(transaccionDTO.idCuentaBancaria(), transaccionDTO.tipo(), transaccionDTO.monto());
                transaccion.setCuentaBancaria(cuenta);
            }

            ZoneId buenosAiresZone = ZoneId.of("America/Argentina/Buenos_Aires");
            ZonedDateTime nowInBuenosAires = ZonedDateTime.now(buenosAiresZone);
            transaccion.setFechaCreacion(nowInBuenosAires.toLocalDateTime());

            if (transaccion.getTipo().equals(TipoTransaccion.INGRESO)) {
                espacio.setSaldo(espacio.getSaldo() + transaccion.getMonto());
            } else {
                espacio.setSaldo(espacio.getSaldo() - transaccion.getMonto());
            }
            espacioRepository.save(espacio);

            transaccion.setEspacioTrabajo(espacio);
            transaccion.setMotivo(motivo);

            Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
            logger.info("Transaccion ID {} registrada exitosamente en espacio ID {}. Nuevo saldo: {}", transaccionGuardada.getId(), espacio.getId(), espacio.getSaldo());
            
            return new TransaccionDTO(
                transaccionGuardada.getId(),
                transaccionGuardada.getFecha(),
                transaccionGuardada.getMonto(),
                transaccionGuardada.getTipo(),
                transaccionGuardada.getDescripcion(),
                transaccionGuardada.getNombreCompletoAuditoria(),
                transaccionGuardada.getEspacioTrabajo().getId(),
                transaccionGuardada.getMotivo().getId(),
                transaccionGuardada.getContacto() != null ? transaccionGuardada.getContacto().getId() : null,
                transaccionGuardada.getCuentaBancaria() != null ? transaccionGuardada.getCuentaBancaria().getId() : null
            );
        } catch (Exception e) {
            logger.error("Error inesperado al registrar transaccion en espacio ID {}: {}", transaccionDTO.idEspacioTrabajo(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void removerTransaccion(Long id) {
        if (id == null) {
            logger.warn("Intento de remover transaccion con ID nulo.");
            throw new IllegalArgumentException("El ID de la transacción no puede ser nulo");
        }
        logger.info("Iniciando remocion de transaccion ID: {}", id);
        try {
            Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "Transaccion con ID " + id + " no encontrada";
                    logger.warn(msg);
                    return new EntityNotFoundException(msg);
                });

            EspacioTrabajo espacio = transaccion.getEspacioTrabajo();
            if (transaccion.getTipo().equals(TipoTransaccion.INGRESO)) {
                espacio.setSaldo(espacio.getSaldo() - transaccion.getMonto());
            } else {
                espacio.setSaldo(espacio.getSaldo() + transaccion.getMonto());
            }

            if(transaccion.getCuentaBancaria() != null) {
                CuentaBancaria cuenta = transaccion.getCuentaBancaria();

                if (transaccion.getTipo() == TipoTransaccion.GASTO) {
                    cuenta.setSaldoActual(cuenta.getSaldoActual() + transaccion.getMonto());
                } else {
                    cuenta.setSaldoActual(cuenta.getSaldoActual() - transaccion.getMonto());
                }
                cuentaBancariaRepository.save(cuenta);
                logger.info("Saldo de cuenta bancaria ID {} actualizado a {} tras remocion de transaccion ID {}", cuenta.getId(), cuenta.getSaldoActual(), id);
            }

            transaccionRepository.delete(transaccion);
            espacioRepository.save(espacio);
            logger.info("Transaccion ID {} removida exitosamente. Saldo del espacio ID {} actualizado a {}", id, espacio.getId(), espacio.getSaldo());
        } catch (Exception e) {
            logger.error("Error inesperado al remover transaccion ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<TransaccionListadoDTO> buscarTransaccion(TransaccionBusquedaDTO datosBusqueda) {
        if (datosBusqueda == null) {
            logger.warn("Intento de buscar transacciones con DTO de busqueda nulo.");
            throw new IllegalArgumentException("Los datos de búsqueda no pueden ser nulos");
        }
        if (datosBusqueda.idEspacioTrabajo() == null) {
            logger.warn("Intento de buscar transacciones con ID de espacio de trabajo nulo.");
            throw new IllegalArgumentException("El ID del espacio de trabajo no puede ser nulo");
        }
        logger.info("Iniciando busqueda de transacciones para espacio ID {} con criterios: {}", datosBusqueda.idEspacioTrabajo(), datosBusqueda);
        try {
            Specification<Transaccion> spec = (root, query, cb) -> cb.equal(root.get("espacioTrabajo").get("id"), datosBusqueda.idEspacioTrabajo());

            if (datosBusqueda.anio() != null) {
                int anio = datosBusqueda.anio();
                int mes = datosBusqueda.mes() != null ? datosBusqueda.mes() : 1;
                java.time.LocalDate desde = java.time.LocalDate.of(anio, mes, 1);
                java.time.LocalDate hasta;
                if (datosBusqueda.mes() != null) {
                    hasta = desde.withDayOfMonth(desde.lengthOfMonth());
                } else {
                    hasta = java.time.LocalDate.of(anio, 12, 31);
                }
                spec = spec.and((root, query, cb) -> cb.between(root.get("fecha"), desde, hasta));
            } else if(datosBusqueda.mes() != null){
                logger.warn("Se especifico mes sin anio en la busqueda de transacciones para espacio ID {}.", datosBusqueda.idEspacioTrabajo());
                throw new IllegalArgumentException("Si no se especifica el año, no se puede especificar el mes");
            }

            if (datosBusqueda.motivo() != null && !datosBusqueda.motivo().isEmpty()) {
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("motivo").get("motivo")), "%" + datosBusqueda.motivo().toLowerCase() + "%"));
            }
            if (datosBusqueda.contacto() != null && !datosBusqueda.contacto().isEmpty()) {
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("contacto").get("nombre")), "%" + datosBusqueda.contacto().toLowerCase() + "%"));
            }

            List<Transaccion> transacciones = transaccionRepository.findAll(spec);
            logger.info("Busqueda de transacciones para espacio ID {} finalizada. Se encontraron {} resultados.", datosBusqueda.idEspacioTrabajo(), transacciones.size());
            return crearListadoTransacciones(transacciones);
        } catch (Exception e) {
            logger.error("Error inesperado durante la busqueda de transacciones para espacio ID {}: {}", datosBusqueda.idEspacioTrabajo(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ContactoDTO registrarContactoTransferencia(ContactoDTO contactoDTO) {
        if (contactoDTO == null) {
            logger.warn("Intento de registrar un ContactoDTO nulo.");
            throw new IllegalArgumentException("El contacto no puede ser nulo");
        }
        logger.info("Iniciando registro de contacto '{}' en espacio ID {}", contactoDTO.nombre(), contactoDTO.idEspacioTrabajo());
        try {
            if (contactoDTO.nombre() == null || contactoDTO.nombre().isEmpty()) {
                logger.warn("Intento de registrar un contacto con nombre nulo o vacio.");
                throw new IllegalArgumentException("El contacto no puede ser nulo");
            }
            if (contactoDTO.idEspacioTrabajo() == null) {
                logger.warn("Intento de registrar un contacto con ID de espacio de trabajo nulo.");
                throw new IllegalArgumentException("El espacio de trabajo del contacto no puede ser nulo");
            }

            ContactoTransferencia contacto = contactoDTO.toContactoTransferencia();

            EspacioTrabajo espacio = espacioRepository.findById(contactoDTO.idEspacioTrabajo()).orElseThrow(() -> {
                String msg = "Espacio de trabajo con ID " + contactoDTO.idEspacioTrabajo() + " no encontrado";
                logger.warn(msg);
                return new EntityNotFoundException(msg);
            });
            contacto.setEspacioTrabajo(espacio);

            ContactoTransferencia contactoGuardado = contactoRepository.save(contacto);
            logger.info("Contacto '{}' (ID: {}) registrado exitosamente en espacio ID {}.", contactoGuardado.getNombre(), contactoGuardado.getId(), espacio.getId());
            return new ContactoDTO(contactoGuardado.getId(), contactoGuardado.getNombre(), contactoGuardado.getEspacioTrabajo().getId());
        } catch (Exception e) {
            logger.error("Error inesperado al registrar contacto '{}' en espacio ID {}: {}", contactoDTO.nombre(), contactoDTO.idEspacioTrabajo(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public MotivoDTO nuevoMotivoTransaccion(MotivoDTO motivoDTO) {
        if (motivoDTO == null) {
            logger.warn("Intento de registrar un MotivoDTO nulo.");
            throw new IllegalArgumentException("El motivo no puede ser nulo");
        }
        logger.info("Iniciando registro de motivo '{}' en espacio ID {}", motivoDTO.motivo(), motivoDTO.idEspacioTrabajo());
        try {
            if (motivoDTO.motivo() == null || motivoDTO.motivo().isEmpty()) {
                logger.warn("Intento de registrar un motivo con nombre nulo o vacio.");
                throw new IllegalArgumentException("El motivo no puede ser nulo");
            }
            if (motivoDTO.idEspacioTrabajo() == null) {
                logger.warn("Intento de registrar un motivo con ID de espacio de trabajo nulo.");
                throw new IllegalArgumentException("El espacio de trabajo del motivo no puede ser nulo");
            }

            MotivoTransaccion motivo = motivoDTO.toMotivoTransaccion();

            EspacioTrabajo espacio = espacioRepository.findById(motivoDTO.idEspacioTrabajo()).orElseThrow(() -> {
                String msg = "Espacio de trabajo con ID " + motivoDTO.idEspacioTrabajo() + " no encontrado";
                logger.warn(msg);
                return new EntityNotFoundException(msg);
            });
            motivo.setEspacioTrabajo(espacio);

            MotivoTransaccion motivoGuardado = motivoRepository.save(motivo);
            logger.info("Motivo '{}' (ID: {}) registrado exitosamente en espacio ID {}.", motivoGuardado.getMotivo(), motivoGuardado.getId(), espacio.getId());
            return new MotivoDTO(motivoGuardado.getId(), motivoGuardado.getMotivo(), motivoGuardado.getEspacioTrabajo().getId());
        } catch (Exception e) {
            logger.error("Error inesperado al registrar motivo '{}' en espacio ID {}: {}", motivoDTO.motivo(), motivoDTO.idEspacioTrabajo(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ContactoListadoDTO> listarContactos(Long idEspacioTrabajo) {
        if (idEspacioTrabajo == null) {
            logger.warn("Intento de listar contactos con ID de espacio de trabajo nulo.");
            throw new IllegalArgumentException("El id del espacio de trabajo no puede ser nulo");
        }
        logger.info("Listando contactos para el espacio de trabajo ID: {}", idEspacioTrabajo);
        try {
            List<ContactoListadoDTO> contactos = contactoRepository.findByEspacioTrabajo_Id(idEspacioTrabajo).stream()
                    .map(contacto -> new ContactoListadoDTO(contacto.getId(), contacto.getNombre()))
                    .toList();
            logger.info("Se encontraron {} contactos para el espacio ID {}.", contactos.size(), idEspacioTrabajo);
            return contactos;
        } catch (Exception e) {
            logger.error("Error inesperado al listar contactos para el espacio ID {}: {}", idEspacioTrabajo, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MotivoListadoDTO> listarMotivos(Long idEspacioTrabajo) {
        if (idEspacioTrabajo == null) {
            logger.warn("Intento de listar motivos con ID de espacio de trabajo nulo.");
            throw new IllegalArgumentException("El id del espacio de trabajo no puede ser nulo");
        }
        logger.info("Listando motivos para el espacio de trabajo ID: {}", idEspacioTrabajo);
        try {
            List<MotivoListadoDTO> motivos = motivoRepository.findByEspacioTrabajo_Id(idEspacioTrabajo).stream()
                    .map(motivo -> new MotivoListadoDTO(motivo.getId(), motivo.getMotivo()))
                    .toList();
            logger.info("Se encontraron {} motivos para el espacio ID {}.", motivos.size(), idEspacioTrabajo);
            return motivos;
        } catch (Exception e) {
            logger.error("Error inesperado al listar motivos para el espacio ID {}: {}", idEspacioTrabajo, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<TransaccionListadoDTO> buscarTransaccionesRecientes(Long idEspacioTrabajo) {
        if (idEspacioTrabajo == null) {
            logger.warn("Intento de buscar transacciones recientes con ID de espacio de trabajo nulo.");
            throw new IllegalArgumentException("El id del espacio de trabajo no puede ser nulo");
        }
        logger.info("Buscando ultimas 6 transacciones para el espacio de trabajo ID: {}", idEspacioTrabajo);
        try {
            java.time.ZoneId buenosAiresZone = java.time.ZoneId.of("America/Argentina/Buenos_Aires");
            java.time.ZonedDateTime nowInBuenosAires = java.time.ZonedDateTime.now(buenosAiresZone);
            java.time.LocalDateTime fechaActual = nowInBuenosAires.toLocalDateTime();

            Specification<Transaccion> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("espacioTrabajo").get("id"), idEspacioTrabajo),
                cb.lessThanOrEqualTo(root.get("fechaCreacion"), fechaActual)
            );

            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 6, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "fechaCreacion"));
            List<Transaccion> transacciones = transaccionRepository.findAll(spec, pageable).getContent();
            logger.info("Se encontraron {} transacciones recientes para el espacio ID {}.", transacciones.size(), idEspacioTrabajo);
            return crearListadoTransacciones(transacciones);
        } catch (Exception e) {
            logger.error("Error inesperado al buscar transacciones recientes para el espacio ID {}: {}", idEspacioTrabajo, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public DashboardInfoDTO obtenerDashboardInfo(Long idEspacio) {
        logger.info("Obteniendo informacion del dashboard para el espacio ID: {}", idEspacio);
        try {
            LocalDate fechaLimite = LocalDate.now().minusMonths(6);

            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM");
            List<String> ultimosMeses = new java.util.ArrayList<>();
            LocalDate actual = LocalDate.now();
            for (int i = 5; i >= 0; i--) {
                ultimosMeses.add(actual.minusMonths(i).format(formatter));
            }

            List<IngresosGastosMesDTO> ingresosGastosMes = dashboardRepository.findIngresosVsGastos(idEspacio, fechaLimite);
            java.util.Map<String, IngresosGastosMesDTO> mapIngresosGastos = new java.util.HashMap<>();
            for (IngresosGastosMesDTO dto : ingresosGastosMes) {
                mapIngresosGastos.put(dto.getMes(), dto);
            }
            List<IngresosGastosMesDTO> ingresosGastosMesCompletos = new java.util.ArrayList<>();
            for (String mes : ultimosMeses) {
                ingresosGastosMesCompletos.add(mapIngresosGastos.getOrDefault(mes, new com.campito.backend.dto.IngresosGastosMesDTOImpl(mes, BigDecimal.ZERO, BigDecimal.ZERO)));
            }

            List<SaldoAcumuladoMesDTO> saldosAcumulados = dashboardRepository.findSaldosAcumulados(idEspacio, fechaLimite);
            java.util.Map<String, SaldoAcumuladoMesDTO> mapSaldos = new java.util.HashMap<>();
            for (SaldoAcumuladoMesDTO dto : saldosAcumulados) {
                mapSaldos.put(dto.getMes(), dto);
            }
            List<SaldoAcumuladoMesDTO> saldosAcumuladosCompletos = new java.util.ArrayList<>();
            for (String mes : ultimosMeses) {
                saldosAcumuladosCompletos.add(mapSaldos.getOrDefault(mes, new com.campito.backend.dto.SaldoAcumuladoMesDTOImpl(mes, BigDecimal.ZERO)));
            }

            List<DistribucionGastoDTO> distribucionGastos = dashboardRepository.findDistribucionGastos(idEspacio, fechaLimite);

            logger.info("Informacion del dashboard para el espacio ID {} generada exitosamente.", idEspacio);
            return new DashboardInfoDTO(
                ingresosGastosMesCompletos,
                distribucionGastos,
                saldosAcumuladosCompletos
            );
        } catch (Exception e) {
            logger.error("Error inesperado al obtener informacion del dashboard para el espacio ID {}: {}", idEspacio, e.getMessage(), e);
            throw e;
        }
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
