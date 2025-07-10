package com.campito.backend.dto;

import com.campito.backend.model.Usuario;

public record UsuarioDTO(
    String nombre,
    String email,
    String clave
) {
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        return usuario;
    }
}
