package com.campito.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contactos_transferencia")
public class ContactoTransferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "id_espacio_trabajo", nullable = false)
    private EspacioTrabajo espacioTrabajo;

    public ContactoTransferencia() {}

    public ContactoTransferencia(String nombre) {
        this.nombre = nombre;
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

    public EspacioTrabajo getEspacioTrabajo() {
        return espacioTrabajo;
    }

    public void setEspacioTrabajo(EspacioTrabajo espacioTrabajo) {
        this.espacioTrabajo = espacioTrabajo;
    }
}
