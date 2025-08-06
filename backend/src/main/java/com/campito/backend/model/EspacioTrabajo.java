package com.campito.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "espacios_trabajo")
public class EspacioTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Column(name = "saldo", nullable = false)
    private Float saldo;
    @ManyToOne
    @JoinColumn(name = "usuario_admin_id", nullable = false)
    private Usuario usuarioAdmin;
    @ManyToMany
    @JoinTable(name = "espacios_trabajo_usuarios", joinColumns = @JoinColumn(name = "espacio_trabajo_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuariosParticipantes;

    public EspacioTrabajo() {}

    public EspacioTrabajo(
        String nombre, 
        Float saldo, 
        Usuario usuarioAdmin) {

        this.nombre = nombre;
        this.saldo = saldo;
        this.usuarioAdmin = usuarioAdmin;
        this.usuariosParticipantes = new ArrayList<>();
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

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public Usuario getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(Usuario usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
    }

    public List<Usuario> getUsuariosParticipantes() {
        return usuariosParticipantes;
    }

    public void addUsuariosParticipante(Usuario usuario) {
        this.usuariosParticipantes.add(usuario);
    }
}
