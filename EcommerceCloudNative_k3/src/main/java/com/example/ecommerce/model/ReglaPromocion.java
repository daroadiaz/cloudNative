package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reglas_promocion")
public class ReglaPromocion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo_regla")
    private String tipoRegla; // STOCK_ALTO, BAJA_ROTACION, TEMPORADA

    @Column(name = "condicion")
    private String condicion; // Expresión de la condición

    @Column(name = "stock_minimo_trigger")
    private Integer stockMinimoTrigger;

    @Column(name = "dias_sin_venta")
    private Integer diasSinVenta;

    @Column(name = "porcentaje_descuento_sugerido")
    private Double porcentajeDescuentoSugerido;

    @Column(name = "activa")
    private Boolean activa;

    @Column(name = "prioridad")
    private Integer prioridad;

    // Constructores
    public ReglaPromocion() {
        this.activa = true;
        this.prioridad = 1;
    }

    public ReglaPromocion(String nombre, String tipoRegla, String condicion) {
        this();
        this.nombre = nombre;
        this.tipoRegla = tipoRegla;
        this.condicion = condicion;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoRegla() {
        return tipoRegla;
    }

    public void setTipoRegla(String tipoRegla) {
        this.tipoRegla = tipoRegla;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Integer getStockMinimoTrigger() {
        return stockMinimoTrigger;
    }

    public void setStockMinimoTrigger(Integer stockMinimoTrigger) {
        this.stockMinimoTrigger = stockMinimoTrigger;
    }

    public Integer getDiasSinVenta() {
        return diasSinVenta;
    }

    public void setDiasSinVenta(Integer diasSinVenta) {
        this.diasSinVenta = diasSinVenta;
    }

    public Double getPorcentajeDescuentoSugerido() {
        return porcentajeDescuentoSugerido;
    }

    public void setPorcentajeDescuentoSugerido(Double porcentajeDescuentoSugerido) {
        this.porcentajeDescuentoSugerido = porcentajeDescuentoSugerido;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }
}