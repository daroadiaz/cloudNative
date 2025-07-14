package com.example.ecommerce.service;

import com.example.ecommerce.dto.StockUpdateEvent;
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
public class KafkaStockConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStockConsumer.class);

    @Autowired
    private MemoriaEstadoService memoriaEstadoService;

    @KafkaListener(
        topics = "${kafka.topic.stock}",
        groupId = "promociones-stock-group",
        containerFactory = "stockKafkaListenerContainerFactory"
    )
    public void consumeStockUpdate(
            @Payload StockUpdateEvent stockUpdate,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            logger.info("Stock update recibido - Topic: {}, Partition: {}, Offset: {}, ProductoId: {}", 
                topic, partition, offset, stockUpdate.getProductoId());
            
            // Actualizar estado en memoria
            memoriaEstadoService.actualizarStock(stockUpdate);
            
            // Confirmar el mensaje
            acknowledgment.acknowledge();
            
            logger.info("Stock update procesado: Producto {} - Stock: {} -> {}", 
                stockUpdate.getProductoId(), 
                stockUpdate.getStockAnterior(), 
                stockUpdate.getStockNuevo());
        } catch (Exception e) {
            logger.error("Error al procesar stock update: {}", e.getMessage(), e);
        }
    }
}