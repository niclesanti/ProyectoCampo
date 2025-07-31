package com.campito.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentas_bancarias")
public class CuentaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "entidad_financiera", nullable = false, length = 50)
    private String entidadFinanciera;

    @Column(name = "saldo_actual", nullable = false)
    private Float saldoActual;
    
    @ManyToOne
    @JoinColumn(name = "id_espacio_trabajo", nullable = false)
    private EspacioTrabajo espacioTrabajo;

    // Getters y Setters
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

    public String getEntidadFinanciera() {
        return entidadFinanciera;
    }

    public void setEntidadFinanciera(String entidadFinanciera) {
        this.entidadFinanciera = entidadFinanciera;
    }

    public Float getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Float saldoActual) {
        this.saldoActual = saldoActual;
    }

    public EspacioTrabajo getEspacioTrabajo() {
        return espacioTrabajo;
    }

    public void setEspacioTrabajo(EspacioTrabajo espacioTrabajo) {
        this.espacioTrabajo = espacioTrabajo;
    }
}
