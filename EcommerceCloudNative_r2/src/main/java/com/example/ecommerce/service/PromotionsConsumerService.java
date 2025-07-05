package com.example.ecommerce.service;

import com.example.ecommerce.model.Promocion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PromotionsConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionsConsumerService.class);

    @Value("${json.output.path}")
    private String jsonOutputPath;

    private final ObjectMapper objectMapper;

    public PromotionsConsumerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @RabbitListener(queues = "${rabbitmq.queue.promotions}")
    public void receivePromotionMessage(Promocion promocion) {
        try {
            logger.info("=== Mensaje de promoción recibido ===");
            logger.info("Código: {}", promocion.getCodigo());
            logger.info("Descripción: {}", promocion.getDescripcion());
            logger.info("Tipo: {}, Valor: {}", promocion.getTipoDescuento(), promocion.getValorDescuento());
            
            // Crear directorio si no existe
            Path outputDir = Paths.get(jsonOutputPath);
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
                logger.info("Directorio creado: {}", outputDir.toAbsolutePath());
            }
            
            // Generar nombre de archivo único
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("promocion_%s_%s.json", promocion.getCodigo(), timestamp);
            Path filePath = outputDir.resolve(fileName);
            
            // Crear objeto con metadata adicional
            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("timestamp_recepcion", LocalDateTime.now());
            jsonData.put("tipo_mensaje", "ACTUALIZACION_PROMOCION");
            jsonData.put("promocion", promocion);
            jsonData.put("metadata", Map.of(
                "procesado_por", "ecommerce-consumer",
                "version", "1.0",
                "ambiente", "produccion"
            ));
            
            // Escribir archivo JSON
            objectMapper.writeValue(filePath.toFile(), jsonData);
            
            logger.info("✓ Archivo JSON generado exitosamente: {}", filePath.toAbsolutePath());
            logger.info("================================");
            
        } catch (IOException e) {
            logger.error("Error al generar archivo JSON: ", e);
        } catch (Exception e) {
            logger.error("Error al procesar mensaje de promoción: ", e);
        }
    }
}