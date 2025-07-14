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
public class KafkaVentasConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaVentasConsumer.class);

    @Autowired
    private MemoriaEstadoService memoriaEstadoService;

    @KafkaListener(
        topics = "${kafka.topic.ventas}",
        groupId = "promociones-ventas-group",
        containerFactory = "ventasKafkaListenerContainerFactory"
    )
    public void consumeVenta(
            @Payload Venta venta,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            logger.info("Venta recibida en Promociones - Topic: {}, Partition: {}, Offset: {}, VentaId: {}", 
                topic, partition, offset, venta.getId());
            
            // Actualizar estado en memoria
            memoriaEstadoService.actualizarVenta(venta);
            
            // Confirmar el mensaje
            acknowledgment.acknowledge();
            
            logger.info("Venta procesada en memoria: {}", venta.getId());
        } catch (Exception e) {
            logger.error("Error al procesar venta en promociones: {}", e.getMessage(), e);
        }
    }
}