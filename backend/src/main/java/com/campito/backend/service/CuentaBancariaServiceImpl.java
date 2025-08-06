package com.campito.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dto.CuentaBancariaDTO;
import com.campito.backend.dto.CuentaBancariaListadoDTO;
import com.campito.backend.model.CuentaBancaria;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.TipoTransaccion;
import com.campito.backend.dao.CuentaBancariaRepository;
import com.campito.backend.dao.EspacioTrabajoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaServiceImpl.class);

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final EspacioTrabajoRepository espacioTrabajoRepository;

    @Autowired
    public CuentaBancariaServiceImpl(
        CuentaBancariaRepository cuentaBancariaRepository,
        EspacioTrabajoRepository espacioTrabajoRepository
    ) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.espacioTrabajoRepository = espacioTrabajoRepository;
    }

    @Override
    @Transactional
    public void crearCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO) {
        if (cuentaBancariaDTO == null) {
            logger.warn("Intento de crear una CuentaBancariaDTO nula.");
            throw new IllegalArgumentException("La cuenta bancaria no puede ser nula");
        }
        logger.info("Creando cuenta bancaria '{}' para entidad '{}'", cuentaBancariaDTO.nombre(), cuentaBancariaDTO.entidadFinanciera());
        try {
            if(cuentaBancariaDTO.idEspacioTrabajo() == null) {
                logger.warn("El idEspacioTrabajo de la cuenta bancaria no puede ser nulo.");
                throw new IllegalArgumentException("El id del espacio de trabajo es requerido para crear la cuenta bancaria");
            }
            if(cuentaBancariaDTO.nombre() == null || cuentaBancariaDTO.nombre().isEmpty()) {
                logger.warn("El nombre de la cuenta bancaria no puede ser nulo o vacío.");
                throw new IllegalArgumentException("El nombre de la cuenta bancaria es requerido");
            }
            if(cuentaBancariaDTO.entidadFinanciera() == null || cuentaBancariaDTO.entidadFinanciera().isEmpty()) {
                logger.warn("La entidad financiera de la cuenta bancaria no puede ser nula o vacía.");
                throw new IllegalArgumentException("La entidad financiera de la cuenta bancaria es requerida");
            }

            EspacioTrabajo espacioTrabajo = espacioTrabajoRepository.findById(cuentaBancariaDTO.idEspacioTrabajo())
                .orElseThrow(() -> {
                    String mensaje = "Espacio de trabajo con ID " + cuentaBancariaDTO.idEspacioTrabajo() + " no encontrado";
                    logger.warn(mensaje);
                    return new EntityNotFoundException(mensaje);
                });

            CuentaBancaria cuentaBancaria = cuentaBancariaDTO.toCuentaBancaria();
            cuentaBancaria.setEspacioTrabajo(espacioTrabajo);
            cuentaBancariaRepository.save(cuentaBancaria);
            logger.info("Cuenta bancaria '{}' creada exitosamente.", cuentaBancaria.getNombre());
        } catch (Exception e) {
            logger.error("Error inesperado al crear cuenta bancaria '{}'.", cuentaBancariaDTO.nombre(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CuentaBancaria actualizarCuentaBancaria(Long id, TipoTransaccion tipo, Float monto) {
        if (id == null || monto == null) {
            logger.warn("Intento de actualizar cuenta bancaria con parametros nulos. ID: {}, Monto: {}", id, monto);
            throw new IllegalArgumentException("El ID y el monto no pueden ser nulos");
        }
        logger.info("Actualizando saldo de cuenta bancaria ID: {} a monto: {}", id, monto);
        try {
            CuentaBancaria cuenta = cuentaBancariaRepository.findById(id)
                .orElseThrow(() -> {
                    String mensaje = "Cuenta bancaria con ID " + id + " no encontrada";
                    logger.warn(mensaje);
                    return new EntityNotFoundException(mensaje);
                });

            if (tipo == TipoTransaccion.GASTO) {
                if (cuenta.getSaldoActual() < monto) {
                    logger.warn("Saldo insuficiente en la cuenta bancaria ID: {} para realizar la actualización de monto: {}", id, monto);
                    throw new IllegalArgumentException("Saldo insuficiente en la cuenta bancaria");
                }
                cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);
            } else {
                cuenta.setSaldoActual(cuenta.getSaldoActual() + monto);
            }

            cuentaBancariaRepository.save(cuenta);
            logger.info("Saldo de cuenta bancaria ID: {} actualizado a {}.", id, cuenta.getSaldoActual());
            return cuenta;
        } catch (Exception e) {
            logger.error("Error al actualizar cuenta bancaria ID: {}. Causa: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<CuentaBancariaListadoDTO> listarCuentasBancarias(Long idEspacioTrabajo) {
        if (idEspacioTrabajo == null) {
            logger.warn("Intento de listar cuentas bancarias con idEspacioTrabajo nulo.");
            throw new IllegalArgumentException("El id del espacio de trabajo no puede ser nulo");
        }
        logger.info("Listando cuentas bancarias para el espacio de trabajo ID: {}", idEspacioTrabajo);
        try {
            List<CuentaBancariaListadoDTO> cuentas = cuentaBancariaRepository.findByEspacioTrabajo_Id(idEspacioTrabajo).stream()
                .map(cuenta -> new CuentaBancariaListadoDTO(
                    cuenta.getId(),
                    cuenta.getNombre(),
                    cuenta.getEntidadFinanciera(),
                    cuenta.getSaldoActual()
                )).toList();
            logger.info("Encontradas {} cuentas bancarias para el espacio de trabajo ID: {}.", cuentas.size(), idEspacioTrabajo);
            return cuentas;
        } catch (Exception e) {
            logger.error("Error al listar cuentas bancarias para el espacio de trabajo ID: {}. Causa: {}", idEspacioTrabajo, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void transaccionEntreCuentas(Long idCuentaOrigen, Long idCuentaDestino, Float monto) {
        if(idCuentaOrigen == null || idCuentaDestino == null || monto == null) {
            logger.warn("Intento de realizar transacción entre cuentas con parámetros nulos. Origen: {}, Destino: {}, Monto: {}", idCuentaOrigen, idCuentaDestino, monto);
            throw new IllegalArgumentException("Los IDs de las cuentas y el monto no pueden ser nulos");
        }

        try {
            CuentaBancaria cuentaOrigen = cuentaBancariaRepository.findById(idCuentaOrigen)
                .orElseThrow(() -> {
                    String mensaje = "Cuenta bancaria origen con ID " + idCuentaOrigen + " no encontrada";
                    logger.warn(mensaje);
                    return new EntityNotFoundException(mensaje);
                });

            CuentaBancaria cuentaDestino = cuentaBancariaRepository.findById(idCuentaDestino)
                .orElseThrow(() -> {
                    String mensaje = "Cuenta bancaria destino con ID " + idCuentaDestino + " no encontrada";
                    logger.warn(mensaje);
                    return new EntityNotFoundException(mensaje);
                });

            if(cuentaOrigen.getSaldoActual() < monto) {
                logger.warn("Saldo insuficiente en la cuenta origen ID: {} para realizar la transacción de monto: {}", idCuentaOrigen, monto);
                throw new IllegalArgumentException("Saldo insuficiente en la cuenta origen");
            }

            cuentaOrigen.setSaldoActual(cuentaOrigen.getSaldoActual() - monto);
            cuentaDestino.setSaldoActual(cuentaDestino.getSaldoActual() + monto);

            cuentaBancariaRepository.save(cuentaOrigen);
            cuentaBancariaRepository.save(cuentaDestino);

            logger.info("Transacción de {} realizada exitosamente entre cuentas ID: {} y ID: {}.", monto, idCuentaOrigen, idCuentaDestino);
        } catch (Exception e) {
            logger.error("Error al realizar transacción entre cuentas. Causa: {}", e.getMessage(), e);
            throw e;
        }
    }

}
