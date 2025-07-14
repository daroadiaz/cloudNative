package com.example.ecommerce.service;

import com.example.ecommerce.dto.StockUpdateEvent;
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

    @Value("${kafka.topic.stock}")
    private String stockTopic;

    public void sendStockUpdate(StockUpdateEvent stockUpdate) {
        try {
            logger.info("Enviando actualización de stock para producto: {}", 
                stockUpdate.getProductoId());
            
            CompletableFuture<SendResult<String, Object>> future = 
                kafkaTemplate.send(stockTopic, 
                    stockUpdate.getProductoId().toString(), 
                    stockUpdate);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Stock update enviado exitosamente: offset={}", 
                        result.getRecordMetadata().offset());
                } else {
                    logger.error("Error al enviar stock update: ", ex);
                }
            });
        } catch (Exception e) {
            logger.error("Error al enviar mensaje de stock a Kafka: ", e);
            throw new RuntimeException("Error al enviar mensaje a Kafka", e);
        }
    }
}