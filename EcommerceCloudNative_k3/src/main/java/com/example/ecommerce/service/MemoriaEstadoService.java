package com.example.ecommerce.service;

import com.example.ecommerce.dto.StockUpdateEvent;
import com.example.ecommerce.model.Venta;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MemoriaEstadoService {

    // Estado en memoria
    private final Map<Long, List<Venta>> ventasPorProducto = new ConcurrentHashMap<>();
    private final Map<Long, StockInfo> stockPorProducto = new ConcurrentHashMap<>();
    private final Map<Long, LocalDateTime> ultimaVentaPorProducto = new ConcurrentHashMap<>();

    // Clase interna para información de stock
    public static class StockInfo {
        private String productoNombre;
        private Integer stockActual;
        private LocalDateTime ultimaActualizacion;
        private List<StockUpdateEvent> historial;

        public StockInfo(String productoNombre, Integer stockActual) {
            this.productoNombre = productoNombre;
            this.stockActual = stockActual;
            this.ultimaActualizacion = LocalDateTime.now();
            this.historial = new ArrayList<>();
        }

        // Getters y setters
        public String getProductoNombre() {
            return productoNombre;
        }

        public void setProductoNombre(String productoNombre) {
            this.productoNombre = productoNombre;
        }

        public Integer getStockActual() {
            return stockActual;
        }

        public void setStockActual(Integer stockActual) {
            this.stockActual = stockActual;
        }

        public LocalDateTime getUltimaActualizacion() {
            return ultimaActualizacion;
        }

        public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
            this.ultimaActualizacion = ultimaActualizacion;
        }

        public List<StockUpdateEvent> getHistorial() {
            return historial;
        }
    }

    // Actualizar información de venta
    public void actualizarVenta(Venta venta) {
        Long productoId = venta.getProductoId();
        
        // Agregar venta al historial
        ventasPorProducto.computeIfAbsent(productoId, k -> new ArrayList<>()).add(venta);
        
        // Actualizar última venta
        ultimaVentaPorProducto.put(productoId, venta.getFechaVenta());
    }

    // Actualizar información de stock
    public void actualizarStock(StockUpdateEvent stockUpdate) {
        Long productoId = stockUpdate.getProductoId();
        
        StockInfo stockInfo = stockPorProducto.computeIfAbsent(productoId, 
            k -> new StockInfo(stockUpdate.getProductoNombre(), stockUpdate.getStockNuevo()));
        
        stockInfo.setStockActual(stockUpdate.getStockNuevo());
        stockInfo.setUltimaActualizacion(LocalDateTime.now());
        stockInfo.getHistorial().add(stockUpdate);
    }

    // Obtener productos con alto stock
    public List<Map<String, Object>> getProductosAltoStock(Integer umbralStock) {
        return stockPorProducto.entrySet().stream()
            .filter(entry -> entry.getValue().getStockActual() > umbralStock)
            .map(entry -> {
                Map<String, Object> producto = new HashMap<>();
                producto.put("productoId", entry.getKey());
                producto.put("productoNombre", entry.getValue().getProductoNombre());
                producto.put("stockActual", entry.getValue().getStockActual());
                producto.put("diasSinVenta", getDiasSinVenta(entry.getKey()));
                return producto;
            })
            .collect(Collectors.toList());
    }

    // Obtener productos con baja rotación
    public List<Map<String, Object>> getProductosBajaRotacion(Integer diasLimite) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasLimite);
        
        return ultimaVentaPorProducto.entrySet().stream()
            .filter(entry -> entry.getValue().isBefore(fechaLimite))
            .map(entry -> {
                Map<String, Object> producto = new HashMap<>();
                producto.put("productoId", entry.getKey());
                
                StockInfo stockInfo = stockPorProducto.get(entry.getKey());
                if (stockInfo != null) {
                    producto.put("productoNombre", stockInfo.getProductoNombre());
                    producto.put("stockActual", stockInfo.getStockActual());
                }
                
                producto.put("ultimaVenta", entry.getValue());
                producto.put("diasSinVenta", getDiasSinVenta(entry.getKey()));
                return producto;
            })
            .collect(Collectors.toList());
    }

    // Obtener productos más vendidos
    public List<Map<String, Object>> getProductosMasVendidos(Integer limite) {
        Map<Long, Integer> ventasTotales = new HashMap<>();
        Map<Long, Double> ingresosTotales = new HashMap<>();
        
        ventasPorProducto.forEach((productoId, ventas) -> {
            int totalCantidad = ventas.stream()
                .mapToInt(Venta::getCantidad)
                .sum();
            double totalIngresos = ventas.stream()
                .mapToDouble(Venta::getPrecioTotal)
                .sum();
                
            ventasTotales.put(productoId, totalCantidad);
            ingresosTotales.put(productoId, totalIngresos);
        });
        
        return ventasTotales.entrySet().stream()
            .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
            .limit(limite)
            .map(entry -> {
                Map<String, Object> producto = new HashMap<>();
                producto.put("productoId", entry.getKey());
                
                StockInfo stockInfo = stockPorProducto.get(entry.getKey());
                if (stockInfo != null) {
                    producto.put("productoNombre", stockInfo.getProductoNombre());
                    producto.put("stockActual", stockInfo.getStockActual());
                }
                
                producto.put("totalVendido", entry.getValue());
                producto.put("ingresosTotales", ingresosTotales.get(entry.getKey()));
                return producto;
            })
            .collect(Collectors.toList());
    }

    // Calcular días sin venta
    private long getDiasSinVenta(Long productoId) {
        LocalDateTime ultimaVenta = ultimaVentaPorProducto.get(productoId);
        if (ultimaVenta == null) {
            return 999; // Sin ventas registradas
        }
        return ChronoUnit.DAYS.between(ultimaVenta, LocalDateTime.now());
    }

    // Obtener estadísticas generales
    public Map<String, Object> getEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProductosMonitoreados", stockPorProducto.size());
        stats.put("totalVentasRegistradas", ventasPorProducto.values().stream()
            .mapToInt(List::size).sum());
        stats.put("productosConAltoStock", getProductosAltoStock(50).size());
        stats.put("productosConBajaRotacion", getProductosBajaRotacion(7).size());
        stats.put("ultimaActualizacion", LocalDateTime.now());
        return stats;
    }

    // Limpiar datos antiguos (opcional, para mantenimiento de memoria)
    public void limpiarDatosAntiguos(Integer diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);
        
        ventasPorProducto.forEach((productoId, ventas) -> {
            ventas.removeIf(venta -> venta.getFechaVenta().isBefore(fechaLimite));
        });
        
        stockPorProducto.forEach((productoId, stockInfo) -> {
            stockInfo.getHistorial().removeIf(
                update -> update.getTimestamp().isBefore(fechaLimite)
            );
        });
    }
}