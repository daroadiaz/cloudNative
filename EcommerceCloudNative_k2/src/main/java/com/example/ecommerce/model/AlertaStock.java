package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_stock")
public class AlertaStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "tipo_alerta")
    private String tipoAlerta; // STOCK_BAJO, STOCK_AGOTADO, SOBRESTOCK

    @Column(name = "stock_actual")
    private Integer stockActual;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha_alerta")
    private LocalDateTime fechaAlerta;

    @Column(name = "estado")
    private String estado; // ACTIVA, RESUELTA

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    // Constructores
    public AlertaStock() {
        this.fechaAlerta = LocalDateTime.now();
        this.estado = "ACTIVA";
    }

    public AlertaStock(Long productoId, String tipoAlerta, Integer stockActual, 
                      Integer stockMinimo, String mensaje) {
        this();
        this.productoId = productoId;
        this.tipoAlerta = tipoAlerta;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(LocalDateTime fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
}