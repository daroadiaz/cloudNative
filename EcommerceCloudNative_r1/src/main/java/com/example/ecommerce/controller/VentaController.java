package com.example.ecommerce.controller;

import com.example.ecommerce.model.Venta;
import com.example.ecommerce.service.RabbitMQProducerService;
import com.example.ecommerce.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private RabbitMQProducerService producerService;

    @Autowired
    private VentaRepository ventaRepository;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarVenta(@RequestBody Venta venta) {
        try {
            // Establecer fecha y estado
            venta.setFechaVenta(LocalDateTime.now());
            venta.setEstado("PROCESADA");
            
            // Calcular precio total
            if (venta.getCantidad() != null && venta.getPrecioUnitario() != null) {
                venta.setPrecioTotal(venta.getCantidad() * venta.getPrecioUnitario());
            }
            
            // Guardar en base de datos local (opcional)
            Venta ventaGuardada = ventaRepository.save(venta);
            
            // Enviar a RabbitMQ
            producerService.sendSalesMessage(ventaGuardada);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Venta procesada y enviada a la cola exitosamente");
            response.put("ventaId", ventaGuardada.getId());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al procesar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Venta Producer");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}