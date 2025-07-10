package com.campito.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dao.UsuarioRepository;
import com.campito.backend.dto.UsuarioDTO;
import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void registrarUsuarioManualmente(UsuarioDTO usuarioDTO) {
        // Buscar el usuario por email y proveedor
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndProveedor(usuarioDTO.email(), ProveedorAutenticacion.MANUAL);

        if (usuarioOpt.isPresent()) {
            // El usuario ya existe, lanzar excepción o manejar según sea necesario
            throw new IllegalArgumentException("El usuario ya está registrado.");
        }

        Usuario usuario = usuarioDTO.toUsuario();
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setFechaUltimoAcceso(LocalDateTime.now());
        usuario.setRol("ROL_USER");
        usuario.setActivo(true);
        usuario.setProveedor(ProveedorAutenticacion.MANUAL);

        // Generar hash para la clave
        String hashedClave = generarHash(usuarioDTO.clave());
        usuario.setIdProveedor(hashedClave);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public boolean iniciarSesion(UsuarioDTO usuarioDTO) {
        // Buscar el usuario por email y proveedor
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndProveedor(usuarioDTO.email(), ProveedorAutenticacion.MANUAL);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verificar si la clave coincide
            String hashedClave = generarHash(usuarioDTO.clave());
            if (usuario.getIdProveedor().equals(hashedClave)) {
                // Actualizar fecha de último acceso
                usuario.setFechaUltimoAcceso(LocalDateTime.now());
                usuarioRepository.save(usuario);
                return true; // Inicio de sesión exitoso
            }
        }
        return false; // Inicio de sesión fallido
    }

    //Metodos auxiliares

    private String generarHash(String clave) {
        return String.valueOf(clave.hashCode());
    }

}
