package com.example.ecommerce.service;

import com.example.ecommerce.model.Venta;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validarVenta(Venta venta) {
        if (venta.getProductoId() == null) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        
        if (venta.getCantidad() == null || venta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        if (venta.getPrecioUnitario() == null || venta.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
        
        if (venta.getClienteEmail() == null || venta.getClienteEmail().isEmpty()) {
            throw new IllegalArgumentException("El email del cliente es requerido");
        }
        
        // Validar formato de email
        if (!venta.getClienteEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del email es inválido");
        }
    }
}