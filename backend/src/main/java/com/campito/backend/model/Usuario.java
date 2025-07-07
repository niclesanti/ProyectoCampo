package com.campito.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    @Enumerated(EnumType.STRING)
    @Column(name = "proveedor")
    private ProveedorAutenticacion proveedor;
    @Column(name = "id_proveedor")
    private String idProveedor;
    @Column(name = "rol", nullable = false)
    private String rol;
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "fecha_ultimo_acceso")
    private LocalDateTime fechaUltimoAcceso;

    public Usuario() {}

    public Usuario(
        String nombre, 
        String email, 
        String fotoPerfil, 
        ProveedorAutenticacion proveedor, 
        String idProveedor, 
        String rol, 
        Boolean activo, 
        LocalDateTime fechaRegistro, 
        LocalDateTime fechaUltimoAcceso) {

        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.proveedor = proveedor;
        this.idProveedor = idProveedor;
        this.rol = rol;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public ProveedorAutenticacion getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorAutenticacion proveedor) {
        this.proveedor = proveedor;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }
}
