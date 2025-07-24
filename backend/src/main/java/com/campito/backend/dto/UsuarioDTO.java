package com.campito.backend.dto;

import com.campito.backend.model.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
    Long id,
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    String nombre,
    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Email(message = "El email debe ser válido")
    String email,
    @NotBlank(message = "La clave no puede estar vacía")
    @Size(min = 6, max = 255, message = "La clave debe contener entre 6 y 255 caracteres")
    String clave
) {
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        return usuario;
    }
}
