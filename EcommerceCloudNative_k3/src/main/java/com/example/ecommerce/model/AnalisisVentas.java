package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "analisis_ventas")
public class AnalisisVentas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "producto_nombre")
    private String productoNombre;

    @Column(name = "total_vendido")
    private Integer totalVendido;

    @Column(name = "ingresos_totales")
    private Double ingresosTotales;

    @Column(name = "promedio_venta_diaria")
    private Double promedioVentaDiaria;

    @Column(name = "ultima_venta")
    private LocalDateTime ultimaVenta;

    @Column(name = "dias_sin_venta")
    private Integer diasSinVenta;

    @Column(name = "velocidad_rotacion")
    private String velocidadRotacion; // ALTA, MEDIA, BAJA

    @Column(name = "fecha_analisis")
    private LocalDateTime fechaAnalisis;

    // Constructores
    public AnalisisVentas() {
        this.fechaAnalisis = LocalDateTime.now();
        this.totalVendido = 0;
        this.ingresosTotales = 0.0;
    }

    public AnalisisVentas(Long productoId, String productoNombre) {
        this();
        this.productoId = productoId;
        this.productoNombre = productoNombre;
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

    public Integer getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(Integer totalVendido) {
        this.totalVendido = totalVendido;
    }

    public Double getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(Double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public Double getPromedioVentaDiaria() {
        return promedioVentaDiaria;
    }

    public void setPromedioVentaDiaria(Double promedioVentaDiaria) {
        this.promedioVentaDiaria = promedioVentaDiaria;
    }

    public LocalDateTime getUltimaVenta() {
        return ultimaVenta;
    }

    public void setUltimaVenta(LocalDateTime ultimaVenta) {
        this.ultimaVenta = ultimaVenta;
    }

    public Integer getDiasSinVenta() {
        return diasSinVenta;
    }

    public void setDiasSinVenta(Integer diasSinVenta) {
        this.diasSinVenta = diasSinVenta;
    }

    public String getVelocidadRotacion() {
        return velocidadRotacion;
    }

    public void setVelocidadRotacion(String velocidadRotacion) {
        this.velocidadRotacion = velocidadRotacion;
    }

    public LocalDateTime getFechaAnalisis() {
        return fechaAnalisis;
    }

    public void setFechaAnalisis(LocalDateTime fechaAnalisis) {
        this.fechaAnalisis = fechaAnalisis;
    }
}