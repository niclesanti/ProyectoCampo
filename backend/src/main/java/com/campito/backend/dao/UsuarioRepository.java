package com.campito.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campito.backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
