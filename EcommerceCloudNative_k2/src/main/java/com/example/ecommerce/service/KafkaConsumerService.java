package com.example.ecommerce.service;

import com.example.ecommerce.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private InventarioService inventarioService;

    @KafkaListener(
        topics = "${kafka.topic.ventas}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeVenta(
            @Payload Venta venta,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            logger.info("Venta recibida - Topic: {}, Partition: {}, Offset: {}, VentaId: {}", 
                topic, partition, offset, venta.getId());
            
            // Procesar la venta y actualizar inventario
            inventarioService.procesarVenta(venta);
            
            // Confirmar el mensaje
            acknowledgment.acknowledge();
            
            logger.info("Venta procesada exitosamente: {}", venta.getId());
        } catch (Exception e) {
            logger.error("Error al procesar venta: {}", e.getMessage(), e);
            // En caso de error, no confirmamos el mensaje para que se reintente
        }
    }
}