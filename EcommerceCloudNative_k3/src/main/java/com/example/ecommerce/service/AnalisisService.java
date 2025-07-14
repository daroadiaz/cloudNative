package com.example.ecommerce.service;

import com.example.ecommerce.model.AnalisisVentas;
import com.example.ecommerce.model.CandidatoPromocion;
import com.example.ecommerce.model.ReglaPromocion;
import com.example.ecommerce.repository.AnalisisVentasRepository;
import com.example.ecommerce.repository.ReglaPromocionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnalisisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalisisService.class);

    @Autowired
    private MemoriaEstadoService memoriaEstadoService;

    @Autowired
    private ReglaPromocionRepository reglaRepository;

    @Autowired
    private AnalisisVentasRepository analisisRepository;

    public List<CandidatoPromocion> analizarCandidatos() {
        logger.info("Analizando candidatos para promociones");
        
        List<CandidatoPromocion> candidatos = new ArrayList<>();
        
        // Obtener reglas activas
        List<ReglaPromocion> reglas = reglaRepository.findByActivaTrue();
        
        for (ReglaPromocion regla : reglas) {
            switch (regla.getTipoRegla()) {
                case "STOCK_ALTO":
                    candidatos.addAll(analizarStockAlto(regla));
                    break;
                case "BAJA_ROTACION":
                    candidatos.addAll(analizarBajaRotacion(regla));
                    break;
                case "TEMPORADA":
                    candidatos.addAll(analizarTemporada(regla));
                    break;
            }
        }
        
        // Actualizar análisis de ventas
        actualizarAnalisisVentas();
        
        return candidatos;
    }

    private List<CandidatoPromocion> analizarStockAlto(ReglaPromocion regla) {
        List<CandidatoPromocion> candidatos = new ArrayList<>();
        
        Integer umbralStock = regla.getStockMinimoTrigger() != null ? 
            regla.getStockMinimoTrigger() : 50;
        
        List<Map<String, Object>> productosAltoStock = 
            memoriaEstadoService.getProductosAltoStock(umbralStock);
        
        for (Map<String, Object> producto : productosAltoStock) {
            CandidatoPromocion candidato = new CandidatoPromocion();
            candidato.setProductoId((Long) producto.get("productoId"));
            candidato.setProductoNombre((String) producto.get("productoNombre"));
            candidato.setStockActual((Integer) producto.get("stockActual"));
            candidato.setDiasSinVenta(((Long) producto.get("diasSinVenta")).intValue());
            candidato.setRazonPromocion("Alto stock: " + producto.get("stockActual") + " unidades");
            candidato.setDescuentoSugerido(regla.getPorcentajeDescuentoSugerido());
            candidato.setPrioridad(regla.getPrioridad());
            
            // Auto-aprobar si cumple criterios
            if (candidato.getStockActual() > umbralStock * 2) {
                candidato.setEstado("APROBADO");
            }
            
            candidatos.add(candidato);
        }
        
        return candidatos;
    }

    private List<CandidatoPromocion> analizarBajaRotacion(ReglaPromocion regla) {
        List<CandidatoPromocion> candidatos = new ArrayList<>();
        
        Integer diasLimite = regla.getDiasSinVenta() != null ? 
            regla.getDiasSinVenta() : 7;
        
        List<Map<String, Object>> productosBajaRotacion = 
            memoriaEstadoService.getProductosBajaRotacion(diasLimite);
        
        for (Map<String, Object> producto : productosBajaRotacion) {
            CandidatoPromocion candidato = new CandidatoPromocion();
            candidato.setProductoId((Long) producto.get("productoId"));
            candidato.setProductoNombre((String) producto.get("productoNombre"));
            candidato.setStockActual((Integer) producto.get("stockActual"));
            candidato.setDiasSinVenta(((Long) producto.get("diasSinVenta")).intValue());
            candidato.setRazonPromocion("Baja rotación: " + 
                producto.get("diasSinVenta") + " días sin venta");
            candidato.setDescuentoSugerido(calcularDescuentoPorRotacion(
                candidato.getDiasSinVenta()));
            candidato.setPrioridad(regla.getPrioridad());
            
            // Auto-aprobar si supera el doble de días límite
            if (candidato.getDiasSinVenta() > diasLimite * 2) {
                candidato.setEstado("APROBADO");
            }
            
            candidatos.add(candidato);
        }
        
        return candidatos;
    }

    private List<CandidatoPromocion> analizarTemporada(ReglaPromocion regla) {
        List<CandidatoPromocion> candidatos = new ArrayList<>();
        
        // Aquí podrías implementar lógica específica de temporada
        // Por ejemplo, productos estacionales, fechas especiales, etc.
        
        return candidatos;
    }

    private Double calcularDescuentoPorRotacion(Integer diasSinVenta) {
        if (diasSinVenta > 30) return 40.0;
        if (diasSinVenta > 14) return 30.0;
        if (diasSinVenta > 7) return 20.0;
        return 15.0;
    }

    private void actualizarAnalisisVentas() {
        List<Map<String, Object>> productosMasVendidos = 
            memoriaEstadoService.getProductosMasVendidos(100);
        
        for (Map<String, Object> producto : productosMasVendidos) {
            AnalisisVentas analisis = new AnalisisVentas();
            analisis.setProductoId((Long) producto.get("productoId"));
            analisis.setProductoNombre((String) producto.get("productoNombre"));
            analisis.setTotalVendido((Integer) producto.get("totalVendido"));
            analisis.setIngresosTotales((Double) producto.get("ingresosTotales"));
            
            // Calcular velocidad de rotación
            Integer totalVendido = analisis.getTotalVendido();
            if (totalVendido > 50) {
                analisis.setVelocidadRotacion("ALTA");
            } else if (totalVendido > 20) {
                analisis.setVelocidadRotacion("MEDIA");
            } else {
                analisis.setVelocidadRotacion("BAJA");
            }
            
            analisisRepository.save(analisis);
        }
    }
}