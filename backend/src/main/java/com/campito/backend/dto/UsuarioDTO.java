package com.campito.backend.dto;

import com.campito.backend.model.Usuario;

public record UsuarioDTO(
    Long id,
    String nombre,
    String email
) {
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }
}
