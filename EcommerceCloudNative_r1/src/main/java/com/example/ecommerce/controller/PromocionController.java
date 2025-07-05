package com.example.ecommerce.controller;

import com.example.ecommerce.model.Promocion;
import com.example.ecommerce.service.RabbitMQProducerService;
import com.example.ecommerce.repository.PromocionRepository;
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
    private RabbitMQProducerService producerService;

    @Autowired
    private PromocionRepository promocionRepository;

    @PostMapping("/actualizar")
    public ResponseEntity<?> actualizarPromocion(@RequestBody Promocion promocion) {
        try {
            // Validar fechas
            if (promocion.getFechaInicio() == null) {
                promocion.setFechaInicio(LocalDateTime.now());
            }
            
            // Guardar en base de datos
            Promocion promocionGuardada = promocionRepository.save(promocion);
            
            // Enviar a RabbitMQ
            producerService.sendPromotionMessage(promocionGuardada);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Promoción actualizada y enviada a la cola exitosamente");
            response.put("promocionId", promocionGuardada.getId());
            response.put("codigo", promocionGuardada.getCodigo());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al actualizar la promoción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<?> enviarPromocionesEnLote(@RequestBody List<Promocion> promociones) {
        try {
            int procesadas = 0;
            for (Promocion promocion : promociones) {
                if (promocion.getFechaInicio() == null) {
                    promocion.setFechaInicio(LocalDateTime.now());
                }
                Promocion saved = promocionRepository.save(promocion);
                producerService.sendPromotionMessage(saved);
                procesadas++;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Promociones procesadas en lote");
            response.put("total", procesadas);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al procesar promociones en lote: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Promocion Producer");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}