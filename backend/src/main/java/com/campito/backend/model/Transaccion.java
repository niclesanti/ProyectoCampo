package com.campito.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTransaccion tipo;

    @Column(name = "monto", nullable = false)
    private Float monto;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "nombre_completo_auditoria", nullable = false, length = 100)
    private String nombreCompletoAuditoria;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "espacio_trabajo_id", nullable = false)
    private EspacioTrabajo espacioTrabajo;

    @ManyToOne
    @JoinColumn(name = "motivo_transaccion_id", nullable = false)
    private MotivoTransaccion motivo;

    @ManyToOne
    @JoinColumn(name = "contacto_transferencia_id")
    private ContactoTransferencia contacto;

    public Transaccion() {
    }

    public Transaccion(
        TipoTransaccion tipo, 
        Float monto, 
        LocalDate fecha, 
        String descripcion, 
        String nombreCompletoAuditoria, 
        LocalDateTime fechaCreacion, 
        EspacioTrabajo espacioTrabajo, 
        MotivoTransaccion motivo, 
        ContactoTransferencia contacto) {
            
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.nombreCompletoAuditoria = nombreCompletoAuditoria;
        this.fechaCreacion = fechaCreacion;
        this.espacioTrabajo = espacioTrabajo;
        this.motivo = motivo;
        this.contacto = contacto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCompletoAuditoria() {
        return nombreCompletoAuditoria;
    }

    public void setNombreCompletoAuditoria(String nombreCompletoAuditoria) {
        this.nombreCompletoAuditoria = nombreCompletoAuditoria;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public ContactoTransferencia getContacto() {
        return contacto;
    }

    public void setContacto(ContactoTransferencia contacto) {
        this.contacto = contacto;
    }
}
