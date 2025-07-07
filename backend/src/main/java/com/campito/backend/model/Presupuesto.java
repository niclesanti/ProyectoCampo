package com.campito.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "presupuestos")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "monto", nullable = false)
    private Float monto;
    @Column(name = "periodo_inicio", nullable = false)
    private LocalDateTime periodoInicio;
    @Column(name = "periodo_fin", nullable = false)
    private LocalDateTime periodoFin;
    @Column(name = "umbral_alerta", nullable = false)
    private Float umbralAlerta;
    @ManyToOne
    @JoinColumn(name = "espacio_trabajo_id", nullable = false)
    private EspacioTrabajo espacioTrabajo;
    @ManyToOne
    @JoinColumn(name = "motivo_id")
    private MotivoTransaccion motivo;

    public Presupuesto() {}

    public Presupuesto(
        Float monto, 
        LocalDateTime periodoInicio, 
        LocalDateTime periodoFin, 
        Float umbralAlerta, 
        EspacioTrabajo espacioTrabajo, 
        MotivoTransaccion motivo) {

        this.monto = monto;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.umbralAlerta = umbralAlerta;
        this.espacioTrabajo = espacioTrabajo;
        this.motivo = motivo;
    }
    
    public Presupuesto(
        Float monto, 
        LocalDateTime periodoInicio, 
        LocalDateTime periodoFin, 
        Float umbralAlerta, 
        EspacioTrabajo espacioTrabajo) {

        this.monto = monto;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.umbralAlerta = umbralAlerta;
        this.espacioTrabajo = espacioTrabajo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDateTime periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDateTime getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(LocalDateTime periodoFin) {
        this.periodoFin = periodoFin;
    }

    public Float getUmbralAlerta() {
        return umbralAlerta;
    }

    public void setUmbralAlerta(Float umbralAlerta) {
        this.umbralAlerta = umbralAlerta;
    }

    public EspacioTrabajo getEspacioTrabajo() {
        return espacioTrabajo;
    }

    public void setEspacioTrabajo(EspacioTrabajo espacioTrabajo) {
        this.espacioTrabajo = espacioTrabajo;
    }

    public MotivoTransaccion getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoTransaccion motivo) {
        this.motivo = motivo;
    }
}
