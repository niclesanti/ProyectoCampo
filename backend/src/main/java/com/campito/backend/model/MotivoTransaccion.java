package com.campito.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "motivos_transaccion")
public class MotivoTransaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "motivo", nullable = false)
    private String motivo;

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
}
