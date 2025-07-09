package com.campito.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "motivos_transaccion")
public class MotivoTransaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "motivo", nullable = false, length = 50)
    private String motivo;
    @ManyToOne
    @JoinColumn(name = "id_espacio_trabajo", nullable = false)
    private EspacioTrabajo espacioTrabajo;

    public MotivoTransaccion() {}

    public MotivoTransaccion(String motivo) {
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EspacioTrabajo getEspacioTrabajo() {
        return espacioTrabajo;
    }

    public void setEspacioTrabajo(EspacioTrabajo espacioTrabajo) {
        this.espacioTrabajo = espacioTrabajo;
    }
}
