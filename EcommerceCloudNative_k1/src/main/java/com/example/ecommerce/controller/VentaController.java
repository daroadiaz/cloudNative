package com.example.ecommerce.controller;

import com.example.ecommerce.model.Venta;
import com.example.ecommerce.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody Venta venta) {
        try {
            Venta ventaProcesada = ventaService.procesarVenta(venta);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Venta registrada exitosamente");
            response.put("ventaId", ventaProcesada.getId());
            response.put("transaccionId", ventaProcesada.getTransaccionId());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al procesar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        return ventaService.obtenerVenta(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarVenta(@PathVariable Long id) {
        try {
            ventaService.cancelarVenta(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Venta cancelada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{email}")
    public ResponseEntity<List<Venta>> ventasPorCliente(@PathVariable String email) {
        return ResponseEntity.ok(ventaService.ventasPorCliente(email));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Venta>> ventasPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        return ResponseEntity.ok(ventaService.ventasPorFecha(fecha));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> procesarVentasBatch(@RequestBody List<Venta> ventas) {
        try {
            ventaService.procesarVentasBatch(ventas);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ventas procesadas en lote");
            response.put("total", ventas.size());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al procesar ventas en lote: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Venta Producer - Kafka");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}