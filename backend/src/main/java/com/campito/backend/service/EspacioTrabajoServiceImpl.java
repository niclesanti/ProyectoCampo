package com.campito.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dao.EspacioTrabajoRepository;
import com.campito.backend.dao.UsuarioRepository;
import com.campito.backend.dto.EspacioTrabajoDTO;
import com.campito.backend.dto.EspacioTrabajoListadoDTO;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.Usuario;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EspacioTrabajoServiceImpl implements EspacioTrabajoService {

    private static final Logger logger = LoggerFactory.getLogger(EspacioTrabajoServiceImpl.class);

    private final EspacioTrabajoRepository espacioRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EspacioTrabajoServiceImpl(
        EspacioTrabajoRepository espacioRepository,
        UsuarioRepository usuarioRepository) {
        this.espacioRepository = espacioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void registrarEspacioTrabajo(EspacioTrabajoDTO espacioTrabajoDTO) {

        if(espacioTrabajoDTO == null) {
            logger.warn("Intento de registrar un EspacioTrabajoDTO nulo.");
            throw new IllegalArgumentException("El espacio de trabajo no puede ser nulo");
        }

        logger.info("Intentando registrar un nuevo espacio de trabajo con nombre: '{}'", espacioTrabajoDTO.nombre());
        try {
            Usuario usuario = usuarioRepository.findById(espacioTrabajoDTO.idUsuarioAdmin()).orElseThrow(() -> {
                String mensaje = "Usuario con ID " + espacioTrabajoDTO.idUsuarioAdmin() + " no encontrado";
                logger.warn(mensaje);
                return new EntityNotFoundException(mensaje);
            });

            EspacioTrabajo espacioTrabajo = new EspacioTrabajo(espacioTrabajoDTO.nombre(), 0f, usuario);
            espacioTrabajo.addUsuariosParticipante(usuario);
            espacioRepository.save(espacioTrabajo);
            logger.info("Espacio de trabajo '{}' registrado exitosamente.", espacioTrabajo.getNombre());
        } catch (Exception e) {
            logger.error("Error inesperado al registrar espacio de trabajo con nombre: '{}'", espacioTrabajoDTO.nombre(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void compartirEspacioTrabajo(String email, Long idEspacioTrabajo, Long idUsuarioAdmin) {
        logger.info("Intentando compartir espacio de trabajo ID: {} con email: {}", idEspacioTrabajo, email);
        try {
            if(email == null || idEspacioTrabajo == null || idUsuarioAdmin == null) {
                logger.warn("Se recibieron parametros nulos para compartir. Email: {}, EspacioID: {}, AdminID: {}", email, idEspacioTrabajo, idUsuarioAdmin);
                throw new IllegalArgumentException("El email, el ID del espacio de trabajo y el ID del usuario administrador del espacio no pueden ser nulos");
            }

            EspacioTrabajo espacioTrabajo = espacioRepository.findById(idEspacioTrabajo).orElseThrow(() -> {
                String mensaje = "Espacio de trabajo con ID " + idEspacioTrabajo + " no encontrado";
                logger.warn(mensaje);
                return new EntityNotFoundException(mensaje);
            });

            if(!espacioTrabajo.getUsuarioAdmin().getId().equals(idUsuarioAdmin)) {
                logger.warn("Intento no autorizado del usuario ID: {} para compartir el espacio ID: {}.", idUsuarioAdmin, idEspacioTrabajo);
                throw new IllegalArgumentException("El usuario administrador no tiene permiso para compartir este espacio de trabajo");
            }

            Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> {
                String mensaje = "Usuario con email " + email + " no encontrado";
                logger.warn(mensaje);
                return new EntityNotFoundException(mensaje);
            });

            espacioTrabajo.addUsuariosParticipante(usuario);

            espacioRepository.save(espacioTrabajo);
            logger.info("Espacio de trabajo ID: {} compartido exitosamente con {}.", idEspacioTrabajo, email);
        } catch (Exception e) {
            logger.error("Error al compartir espacio de trabajo ID: {} con email: {}. Causa: {}", idEspacioTrabajo, email, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<EspacioTrabajoListadoDTO> listarEspaciosTrabajoPorUsuario(Long idUsuario) {
        logger.info("Intentando listar espacios de trabajo para el usuario ID: {}", idUsuario);
        try {
            List<EspacioTrabajo> espacios = espacioRepository.findByUsuariosParticipantes_Id(idUsuario);
            logger.info("Encontrados {} espacios de trabajo para el usuario ID: {}.", espacios.size(), idUsuario);
            return espacios.stream()
                .map(espacio -> new EspacioTrabajoListadoDTO(
                    espacio.getId(),
                    espacio.getNombre(),
                    espacio.getSaldo(),
                    espacio.getUsuarioAdmin().getId()
                ))
                .toList();
        } catch (Exception e) {
            logger.error("Error al listar espacios de trabajo para el usuario ID: {}. Causa: {}", idUsuario, e.getMessage(), e);
            throw e;
        }
    }

}
