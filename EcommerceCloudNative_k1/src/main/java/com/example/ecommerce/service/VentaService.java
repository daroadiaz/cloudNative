package com.example.ecommerce.service;

import com.example.ecommerce.model.Venta;
import com.example.ecommerce.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Venta procesarVenta(Venta venta) {
        // Validar datos
        validationService.validarVenta(venta);
        
        // Generar ID único de transacción
        venta.setTransaccionId(UUID.randomUUID().toString());
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado("PROCESADA");
        
        // Calcular total
        if (venta.getCantidad() != null && venta.getPrecioUnitario() != null) {
            venta.setPrecioTotal(venta.getCantidad() * venta.getPrecioUnitario());
        }
        
        // Guardar en base de datos
        Venta ventaGuardada = ventaRepository.save(venta);
        
        // Enviar a Kafka
        kafkaProducerService.sendVentaMessage(ventaGuardada);
        
        return ventaGuardada;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> obtenerVenta(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta actualizarVenta(Long id, Venta venta) {
        Optional<Venta> ventaExistente = ventaRepository.findById(id);
        if (ventaExistente.isPresent()) {
            Venta ventaActualizada = ventaExistente.get();
            // Actualizar campos necesarios
            ventaActualizada.setEstado(venta.getEstado());
            ventaActualizada = ventaRepository.save(ventaActualizada);
            
            // Enviar actualización a Kafka
            kafkaProducerService.sendVentaMessage(ventaActualizada);
            
            return ventaActualizada;
        }
        throw new RuntimeException("Venta no encontrada");
    }

    public void cancelarVenta(Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        if (venta.isPresent()) {
            Venta ventaCancelada = venta.get();
            ventaCancelada.setEstado("CANCELADA");
            ventaRepository.save(ventaCancelada);
            
            // Enviar cancelación a Kafka
            kafkaProducerService.sendVentaMessage(ventaCancelada);
        }
    }

    public List<Venta> ventasPorCliente(String clienteEmail) {
        return ventaRepository.findByClienteEmail(clienteEmail);
    }

    public List<Venta> ventasPorFecha(LocalDateTime fecha) {
        return ventaRepository.findByFechaVentaBetween(
            fecha.withHour(0).withMinute(0),
            fecha.withHour(23).withMinute(59)
        );
    }

    @Transactional
    public void procesarVentasBatch(List<Venta> ventas) {
        for (Venta venta : ventas) {
            procesarVenta(venta);
        }
    }
}