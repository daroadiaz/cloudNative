package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "ventas")
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "producto_nombre")
    private String productoNombre;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column(name = "precio_total")
    private Double precioTotal;

    @Column(name = "cliente_nombre")
    private String clienteNombre;

    @Column(name = "cliente_email")
    private String clienteEmail;

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @Column(name = "estado")
    private String estado; // PENDIENTE, PROCESADA, CANCELADA

    // Constructores
    public Venta() {
        this.fechaVenta = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public Venta(Long productoId, String productoNombre, Integer cantidad, 
                 Double precioUnitario, String clienteNombre, String clienteEmail) {
        this();
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = cantidad * precioUnitario;
        this.clienteNombre = clienteNombre;
        this.clienteEmail = clienteEmail;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}