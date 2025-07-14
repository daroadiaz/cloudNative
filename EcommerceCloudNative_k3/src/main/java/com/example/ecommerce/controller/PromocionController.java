package com.example.ecommerce.controller;

import com.example.ecommerce.model.CandidatoPromocion;
import com.example.ecommerce.model.Promocion;
import com.example.ecommerce.service.AnalisisService;
import com.example.ecommerce.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/promociones")
@CrossOrigin(origins = "*")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @Autowired
    private AnalisisService analisisService;

    @PostMapping("/generar")
    public ResponseEntity<?> generarPromociones() {
        try {
            List<Promocion> promocionesGeneradas = promocionService.generarPromociones();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Promociones generadas exitosamente");
            response.put("promocionesCreadas", promocionesGeneradas.size());
            response.put("promociones", promocionesGeneradas);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al generar promociones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Promocion>> listarPromociones() {
        return ResponseEntity.ok(promocionService.listarPromociones());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Promocion>> listarPromocionesActivas() {
        return ResponseEntity.ok(promocionService.listarPromocionesActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPromocion(@PathVariable Long id) {
        try {
            Promocion promocion = promocionService.obtenerPromocion(id);
            return ResponseEntity.ok(promocion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPromocion(@PathVariable Long id, 
                                               @RequestBody Promocion promocion) {
        try {
            Promocion promocionActualizada = promocionService.actualizarPromocion(id, promocion);
            return ResponseEntity.ok(promocionActualizada);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarPromocion(@PathVariable Long id) {
        try {
            promocionService.desactivarPromocion(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Promoción desactivada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Promocion>> obtenerPromocionesPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(promocionService.obtenerPromocionesPorProducto(productoId));
    }

    @PostMapping("/analizar")
    public ResponseEntity<?> analizarCandidatos() {
        try {
            List<CandidatoPromocion> candidatos = analisisService.analizarCandidatos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Análisis completado");
            response.put("candidatosIdentificados", candidatos.size());
            response.put("candidatos", candidatos);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al analizar candidatos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(promocionService.obtenerEstadisticasPromociones());
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Promociones Service - Kafka Consumer");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}