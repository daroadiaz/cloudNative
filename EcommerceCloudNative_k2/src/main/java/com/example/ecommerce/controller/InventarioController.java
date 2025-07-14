package com.example.ecommerce.controller;

import com.example.ecommerce.model.Producto;
import com.example.ecommerce.model.MovimientoInventario;
import com.example.ecommerce.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarInventario() {
        return ResponseEntity.ok(inventarioService.listarInventario());
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long productoId) {
        return inventarioService.obtenerProducto(productoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/productos")
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = inventarioService.agregarProducto(producto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto agregado exitosamente");
            response.put("productoId", nuevoProducto.getId());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al agregar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<?> actualizarStock(@PathVariable Long productoId, 
                                           @RequestParam Integer nuevoStock,
                                           @RequestParam String motivo) {
        try {
            Producto producto = inventarioService.actualizarStock(productoId, nuevoStock, motivo);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long productoId) {
        try {
            inventarioService.eliminarProducto(productoId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<Producto>> productosBajoStock() {
        return ResponseEntity.ok(inventarioService.obtenerProductosBajoStock());
    }

    @PostMapping("/restock/{productoId}")
    public ResponseEntity<?> reabastecerProducto(@PathVariable Long productoId, 
                                               @RequestParam Integer cantidad) {
        try {
            Producto producto = inventarioService.reabastecerProducto(productoId, cantidad);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto reabastecido exitosamente");
            response.put("stockNuevo", producto.getStockActual());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventario>> listarMovimientos() {
        return ResponseEntity.ok(inventarioService.obtenerMovimientos());
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Inventario Service - Kafka");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}