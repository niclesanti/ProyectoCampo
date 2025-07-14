package com.campito.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String nombre;
    private final String email;
    private final String password;

    public CustomUserDetails(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.password = usuario.getIdProveedor(); // En tu caso, idProveedor es la clave hasheada
    }

    // Métodos para obtener tus datos personalizados
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Métodos de la interfaz UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por ahora, no manejamos roles complejos, devolvemos una lista vacía.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // Usamos el email como nombre de usuario para Spring Security
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
