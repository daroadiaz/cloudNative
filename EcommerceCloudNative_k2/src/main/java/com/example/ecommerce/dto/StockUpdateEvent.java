package com.example.ecommerce.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class StockUpdateEvent implements Serializable {
    private Long productoId;
    private String productoNombre;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String tipoMovimiento;
    private String motivo;
    private LocalDateTime timestamp;

    // Constructores
    public StockUpdateEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public StockUpdateEvent(Long productoId, String productoNombre, Integer stockAnterior, 
                           Integer stockNuevo, String tipoMovimiento, String motivo) {
        this();
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.tipoMovimiento = tipoMovimiento;
        this.motivo = motivo;
    }

    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
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

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}