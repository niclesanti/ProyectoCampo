package com.campito.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dao.EspacioTrabajoRepository;
import com.campito.backend.dao.UsuarioRepository;
import com.campito.backend.dto.EspacioTrabajoDTO;
import com.campito.backend.model.EspacioTrabajo;
import com.campito.backend.model.Usuario;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EspacioTrabajoServiceImpl implements EspacioTrabajoService {

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
            throw new IllegalArgumentException("El espacio de trabajo no puede ser nulo");
        }

        Usuario usuario = usuarioRepository.findById(espacioTrabajoDTO.idUsuarioAdmin()).orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + espacioTrabajoDTO.idUsuarioAdmin() + " no encontrado"));

        EspacioTrabajo espacioTrabajo = new EspacioTrabajo(espacioTrabajoDTO.nombre(), 0f, usuario);
        espacioTrabajo.addUsuariosParticipante(usuario);
        espacioRepository.save(espacioTrabajo);
    }

    @Override
    @Transactional
    public void compartirEspacioTrabajo(Long idUsuario, Long idEspacioTrabajo, Long idUsuarioAdmin) {
        if(idUsuario == null || idEspacioTrabajo == null || idUsuarioAdmin == null) {
            throw new IllegalArgumentException("El ID del usuario, el ID del espacio de trabajo y el ID del usuario administrador del espacio no pueden ser nulos");
        }

        EspacioTrabajo espacioTrabajo = espacioRepository.findById(idEspacioTrabajo).orElseThrow(() -> new EntityNotFoundException("Espacio de trabajo con ID " + idEspacioTrabajo + " no encontrado"));

        if(!espacioTrabajo.getUsuarioAdmin().getId().equals(idUsuarioAdmin)) {
            throw new IllegalArgumentException("El usuario administrador no tiene permiso para compartir este espacio de trabajo");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + idUsuario + " no encontrado"));

        espacioTrabajo.addUsuariosParticipante(usuario);

        espacioRepository.save(espacioTrabajo);
    }

}
