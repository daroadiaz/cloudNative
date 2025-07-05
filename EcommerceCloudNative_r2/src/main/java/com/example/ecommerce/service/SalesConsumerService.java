package com.example.ecommerce.service;

import com.example.ecommerce.model.Venta;
import com.example.ecommerce.repository.VentaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class SalesConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(SalesConsumerService.class);

    @Autowired
    private VentaRepository ventaRepository;

    @RabbitListener(queues = "${rabbitmq.queue.sales}")
    public void receiveSalesMessage(Venta venta) {
        try {
            logger.info("=== Mensaje de venta recibido ===");
            logger.info("ID: {}", venta.getId());
            logger.info("Producto: {} (ID: {})", venta.getProductoNombre(), venta.getProductoId());
            logger.info("Cliente: {} ({})", venta.getClienteNombre(), venta.getClienteEmail());
            logger.info("Cantidad: {}, Precio unitario: ${}, Total: ${}", 
                venta.getCantidad(), venta.getPrecioUnitario(), venta.getPrecioTotal());
            
            // Actualizar información antes de guardar
            venta.setId(null); // Resetear ID para crear nuevo registro
            venta.setFechaVenta(LocalDateTime.now());
            venta.setEstado("RECIBIDA_EN_CONSUMIDOR");
            
            // Guardar en Oracle Cloud
            Venta ventaGuardada = ventaRepository.save(venta);
            
            logger.info("✓ Venta guardada exitosamente en Oracle Cloud con nuevo ID: {}", 
                ventaGuardada.getId());
            logger.info("================================");
            
        } catch (Exception e) {
            logger.error("Error al procesar mensaje de venta: ", e);
            // En producción, podrías implementar un mecanismo de reintento o dead letter queue
        }
    }
}