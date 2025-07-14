package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "tipo_movimiento")
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "stock_anterior")
    private Integer stockAnterior;

    @Column(name = "stock_nuevo")
    private Integer stockNuevo;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "referencia_id")
    private String referenciaId; // ID de venta, compra, etc.

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    @Column(name = "usuario")
    private String usuario;

    // Constructores
    public MovimientoInventario() {
        this.fechaMovimiento = LocalDateTime.now();
    }

    public MovimientoInventario(Long productoId, String tipoMovimiento, Integer cantidad, 
                               Integer stockAnterior, Integer stockNuevo, String motivo) {
        this();
        this.productoId = productoId;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.motivo = motivo;
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

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getStockNuevo() {
        return stockNuevo;
    }

    public void setStockNuevo(Integer stockNuevo) {
        this.stockNuevo = stockNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(String referenciaId) {
        this.referenciaId = referenciaId;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}