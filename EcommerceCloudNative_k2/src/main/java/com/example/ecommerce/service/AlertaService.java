package com.example.ecommerce.service;

import com.example.ecommerce.model.AlertaStock;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.repository.AlertaStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    private static final Logger logger = LoggerFactory.getLogger(AlertaService.class);

    @Autowired
    private AlertaStockRepository alertaRepository;

    public void verificarStock(Producto producto) {
        // Resolver alertas anteriores si el stock se recuperó
        List<AlertaStock> alertasActivas = alertaRepository
            .findByProductoIdAndEstado(producto.getId(), "ACTIVA");
        
        if (producto.getStockActual() > producto.getStockMinimo()) {
            for (AlertaStock alerta : alertasActivas) {
                alerta.setEstado("RESUELTA");
                alerta.setFechaResolucion(LocalDateTime.now());
                alertaRepository.save(alerta);
                logger.info("Alerta resuelta para producto {}", producto.getId());
            }
        } else {
            // Verificar si necesita crear nueva alerta
            if (alertasActivas.isEmpty()) {
                String tipoAlerta;
                String mensaje;
                
                if (producto.getStockActual() == 0) {
                    tipoAlerta = "STOCK_AGOTADO";
                    mensaje = String.format("Producto %s sin stock disponible", 
                        producto.getNombre());
                } else if (producto.getStockActual() <= producto.getStockMinimo()) {
                    tipoAlerta = "STOCK_BAJO";
                    mensaje = String.format("Stock bajo para %s: %d unidades (mínimo: %d)", 
                        producto.getNombre(), 
                        producto.getStockActual(), 
                        producto.getStockMinimo());
                } else {
                    return; // No se necesita alerta
                }
                
                AlertaStock nuevaAlerta = new AlertaStock(
                    producto.getId(),
                    tipoAlerta,
                    producto.getStockActual(),
                    producto.getStockMinimo(),
                    mensaje
                );
                
                alertaRepository.save(nuevaAlerta);
                logger.warn("Alerta creada: {}", mensaje);
            }
        }
    }

    public List<AlertaStock> obtenerAlertasActivas() {
        return alertaRepository.findByEstado("ACTIVA");
    }
}