package com.example.ecommerce.service;

import com.example.ecommerce.dto.StockUpdateEvent;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private AlertaStockRepository alertaRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private AlertaService alertaService;

    public void procesarVenta(Venta venta) {
        logger.info("Procesando venta {} para producto {}", venta.getId(), venta.getProductoId());
        
        Optional<Producto> productoOpt = productoRepository.findById(venta.getProductoId());
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            Integer stockAnterior = producto.getStockActual();
            Integer stockNuevo = stockAnterior - venta.getCantidad();
            
            // Validar stock disponible
            if (stockNuevo < 0) {
                logger.error("Stock insuficiente para producto {}: stock actual={}, cantidad solicitada={}", 
                    producto.getId(), stockAnterior, venta.getCantidad());
                throw new RuntimeException("Stock insuficiente para el producto " + producto.getNombre());
            }
            
            // Actualizar stock
            producto.setStockActual(stockNuevo);
            producto.setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto);
            
            // Registrar movimiento
            MovimientoInventario movimiento = new MovimientoInventario(
                producto.getId(),
                "SALIDA",
                venta.getCantidad(),
                stockAnterior,
                stockNuevo,
                "Venta #" + venta.getId()
            );
            movimiento.setReferenciaId(venta.getTransaccionId());
            movimiento.setUsuario("SISTEMA");
            movimientoRepository.save(movimiento);
            
            // Verificar alertas
            alertaService.verificarStock(producto);
            
            // Publicar evento de actualización de stock
            StockUpdateEvent stockUpdate = new StockUpdateEvent(
                producto.getId(),
                producto.getNombre(),
                stockAnterior,
                stockNuevo,
                "VENTA",
                "Venta procesada: " + venta.getId()
            );
            kafkaProducerService.sendStockUpdate(stockUpdate);
            
            logger.info("Stock actualizado para producto {}: {} -> {}", 
                producto.getId(), stockAnterior, stockNuevo);
        } else {
            logger.error("Producto no encontrado: {}", venta.getProductoId());
            // Aquí podrías enviar una notificación o crear un registro de error
        }
    }

    public List<Producto> listarInventario() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProducto(Long id) {
        return productoRepository.findById(id);
    }

    public Producto agregarProducto(Producto producto) {
        producto.setFechaActualizacion(LocalDateTime.now());
        Producto productoGuardado = productoRepository.save(producto);
        
        // Registrar movimiento inicial
        MovimientoInventario movimiento = new MovimientoInventario(
            productoGuardado.getId(),
            "ENTRADA",
            productoGuardado.getStockActual(),
            0,
            productoGuardado.getStockActual(),
            "Stock inicial"
        );
        movimientoRepository.save(movimiento);
        
        // Publicar evento
        StockUpdateEvent stockUpdate = new StockUpdateEvent(
            productoGuardado.getId(),
            productoGuardado.getNombre(),
            0,
            productoGuardado.getStockActual(),
            "NUEVO_PRODUCTO",
            "Producto agregado al inventario"
        );
        kafkaProducerService.sendStockUpdate(stockUpdate);
        
        return productoGuardado;
    }

    public Producto actualizarStock(Long productoId, Integer nuevoStock, String motivo) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            Integer stockAnterior = producto.getStockActual();
            
            // Actualizar stock
            producto.setStockActual(nuevoStock);
            producto.setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto);
            
            // Registrar movimiento
            String tipoMovimiento = nuevoStock > stockAnterior ? "ENTRADA" : "SALIDA";
            MovimientoInventario movimiento = new MovimientoInventario(
                producto.getId(),
                tipoMovimiento,
                Math.abs(nuevoStock - stockAnterior),
                stockAnterior,
                nuevoStock,
                motivo
            );
            movimientoRepository.save(movimiento);
            
            // Verificar alertas
            alertaService.verificarStock(producto);
            
            // Publicar evento
            StockUpdateEvent stockUpdate = new StockUpdateEvent(
                producto.getId(),
                producto.getNombre(),
                stockAnterior,
                nuevoStock,
                "AJUSTE_MANUAL",
                motivo
            );
            kafkaProducerService.sendStockUpdate(stockUpdate);
            
            return producto;
        }
        
        throw new RuntimeException("Producto no encontrado");
    }

    public Producto reabastecerProducto(Long productoId, Integer cantidad) {
        return actualizarStock(productoId, 
            obtenerProducto(productoId).get().getStockActual() + cantidad, 
            "Reabastecimiento");
    }

    public void eliminarProducto(Long productoId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setActivo(false);
            productoRepository.save(producto);
            
            // Publicar evento
            StockUpdateEvent stockUpdate = new StockUpdateEvent(
                producto.getId(),
                producto.getNombre(),
                producto.getStockActual(),
                0,
                "PRODUCTO_ELIMINADO",
                "Producto marcado como inactivo"
            );
            kafkaProducerService.sendStockUpdate(stockUpdate);
        }
    }

    public List<Producto> obtenerProductosBajoStock() {
        return productoRepository.findByStockActualLessThanEqual(10);
    }

    public List<MovimientoInventario> obtenerMovimientos() {
        return movimientoRepository.findAllByOrderByFechaMovimientoDesc();
    }
}