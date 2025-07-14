package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatos_promocion")
public class CandidatoPromocion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "producto_nombre")
    private String productoNombre;

    @Column(name = "stock_actual")
    private Integer stockActual;

    @Column(name = "dias_sin_venta")
    private Integer diasSinVenta;

    @Column(name = "razon_promocion")
    private String razonPromocion;

    @Column(name = "descuento_sugerido")
    private Double descuentoSugerido;

    @Column(name = "prioridad")
    private Integer prioridad;

    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;

    @Column(name = "estado")
    private String estado; // PENDIENTE, APROBADO, RECHAZADO

    // Constructores
    public CandidatoPromocion() {
        this.fechaEvaluacion = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.prioridad = 1;
    }

    public CandidatoPromocion(Long productoId, String productoNombre, Integer stockActual, 
                             String razonPromocion, Double descuentoSugerido) {
        this();
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.stockActual = stockActual;
        this.razonPromocion = razonPromocion;
        this.descuentoSugerido = descuentoSugerido;
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

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getDiasSinVenta() {
        return diasSinVenta;
    }

    public void setDiasSinVenta(Integer diasSinVenta) {
        this.diasSinVenta = diasSinVenta;
    }

    public String getRazonPromocion() {
        return razonPromocion;
    }

    public void setRazonPromocion(String razonPromocion) {
        this.razonPromocion = razonPromocion;
    }

    public Double getDescuentoSugerido() {
        return descuentoSugerido;
    }

    public void setDescuentoSugerido(Double descuentoSugerido) {
        this.descuentoSugerido = descuentoSugerido;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}