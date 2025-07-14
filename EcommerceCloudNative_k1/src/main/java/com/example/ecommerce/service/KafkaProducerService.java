package com.example.ecommerce.service;

import com.example.ecommerce.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.ventas}")
    private String ventasTopic;

    public void sendVentaMessage(Venta venta) {
        try {
            logger.info("Enviando venta a Kafka topic '{}': {}", ventasTopic, venta.getId());
            
            CompletableFuture<SendResult<String, Object>> future = 
                kafkaTemplate.send(ventasTopic, venta.getProductoId().toString(), venta);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Venta enviada exitosamente: offset={}", 
                        result.getRecordMetadata().offset());
                } else {
                    logger.error("Error al enviar venta: ", ex);
                }
            });
        } catch (Exception e) {
            logger.error("Error al enviar mensaje a Kafka: ", e);
            throw new RuntimeException("Error al enviar mensaje a Kafka", e);
        }
    }
}