package com.campito.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campito.backend.model.Usuario;
import java.util.Optional;
import com.campito.backend.model.ProveedorAutenticacion;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndProveedor(String email, ProveedorAutenticacion proveedor);
}
