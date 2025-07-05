package com.example.ecommerce.service;

import com.example.ecommerce.model.Venta;
import com.example.ecommerce.model.Promocion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RabbitMQProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducerService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.sales}")
    private String salesRoutingKey;

    @Value("${rabbitmq.routing.promotions}")
    private String promotionsRoutingKey;

    public RabbitMQProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSalesMessage(Venta venta) {
        try {
            logger.info("Enviando mensaje de venta a RabbitMQ: {}", venta.getId());
            rabbitTemplate.convertAndSend(exchange, salesRoutingKey, venta);
            logger.info("Mensaje de venta enviado exitosamente");
        } catch (Exception e) {
            logger.error("Error al enviar mensaje de venta: ", e);
            throw new RuntimeException("Error al enviar mensaje a RabbitMQ", e);
        }
    }

    public void sendPromotionMessage(Promocion promocion) {
        try {
            logger.info("Enviando mensaje de promoción a RabbitMQ: {}", promocion.getCodigo());
            rabbitTemplate.convertAndSend(exchange, promotionsRoutingKey, promocion);
            logger.info("Mensaje de promoción enviado exitosamente");
        } catch (Exception e) {
            logger.error("Error al enviar mensaje de promoción: ", e);
            throw new RuntimeException("Error al enviar mensaje a RabbitMQ", e);
        }
    }
}