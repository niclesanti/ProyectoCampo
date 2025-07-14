package com.campito.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campito.backend.dao.UsuarioRepository;
import com.campito.backend.dto.UsuarioDTO;
import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Codificar la clave con BCrypt
        String encodedClave = passwordEncoder.encode(usuarioDTO.clave());
        usuario.setIdProveedor(encodedClave);

        usuarioRepository.save(usuario);
    }
}
