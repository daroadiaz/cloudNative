package com.example.ecommerce.service;

import com.example.ecommerce.model.CandidatoPromocion;
import com.example.ecommerce.model.Promocion;
import com.example.ecommerce.repository.CandidatoPromocionRepository;
import com.example.ecommerce.repository.PromocionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PromocionService {

    private static final Logger logger = LoggerFactory.getLogger(PromocionService.class);

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private CandidatoPromocionRepository candidatoRepository;

    @Autowired
    private MemoriaEstadoService memoriaEstadoService;

    @Autowired
    private AnalisisService analisisService;

    public List<Promocion> generarPromociones() {
        logger.info("Iniciando generación de promociones ON DEMAND");
        
        List<Promocion> nuevasPromociones = new ArrayList<>();
        
        // Analizar candidatos
        List<CandidatoPromocion> candidatos = analisisService.analizarCandidatos();
        
        for (CandidatoPromocion candidato : candidatos) {
            if ("APROBADO".equals(candidato.getEstado())) {
                Promocion promocion = crearPromocionDesdeCandidato(candidato);
                promocion = promocionRepository.save(promocion);
                nuevasPromociones.add(promocion);
                
                logger.info("Promoción creada: {} para producto {}", 
                    promocion.getCodigo(), candidato.getProductoId());
            }
        }
        
        // Guardar candidatos analizados
        candidatoRepository.saveAll(candidatos);
        
        logger.info("Promociones generadas: {}", nuevasPromociones.size());
        return nuevasPromociones;
    }

    private Promocion crearPromocionDesdeCandidato(CandidatoPromocion candidato) {
        Promocion promocion = new Promocion();
        
        // Generar código único
        promocion.setCodigo("PROMO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Configurar promoción basada en el candidato
        promocion.setDescripcion(String.format("%s - %s", 
            candidato.getRazonPromocion(), candidato.getProductoNombre()));
        
        promocion.setProductoId(candidato.getProductoId());
        promocion.setTipoDescuento("PORCENTAJE");
        promocion.setValorDescuento(candidato.getDescuentoSugerido());
        
        // Fechas de vigencia (7 días por defecto)
        promocion.setFechaInicio(LocalDateTime.now());
        promocion.setFechaFin(LocalDateTime.now().plusDays(7));
        
        promocion.setActivo(true);
        promocion.setCategoria(determinarCategoria(candidato.getRazonPromocion()));
        
        return promocion;
    }

    private String determinarCategoria(String razonPromocion) {
        if (razonPromocion.contains("stock")) {
            return "LIQUIDACION";
        } else if (razonPromocion.contains("rotación")) {
            return "IMPULSO";
        } else if (razonPromocion.contains("temporada")) {
            return "TEMPORADA";
        }
        return "GENERAL";
    }

    public List<Promocion> listarPromociones() {
        return promocionRepository.findAll();
    }

    public List<Promocion> listarPromocionesActivas() {
        return promocionRepository.findByActivoTrueAndFechaFinAfter(LocalDateTime.now());
    }

    public Promocion obtenerPromocion(Long id) {
        return promocionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
    }

    public Promocion actualizarPromocion(Long id, Promocion promocionActualizada) {
        Promocion promocion = obtenerPromocion(id);
        
        // Actualizar campos permitidos
        if (promocionActualizada.getDescripcion() != null) {
            promocion.setDescripcion(promocionActualizada.getDescripcion());
        }
        if (promocionActualizada.getValorDescuento() != null) {
            promocion.setValorDescuento(promocionActualizada.getValorDescuento());
        }
        if (promocionActualizada.getFechaFin() != null) {
            promocion.setFechaFin(promocionActualizada.getFechaFin());
        }
        
        return promocionRepository.save(promocion);
    }

    public void desactivarPromocion(Long id) {
        Promocion promocion = obtenerPromocion(id);
        promocion.setActivo(false);
        promocionRepository.save(promocion);
        logger.info("Promoción {} desactivada", promocion.getCodigo());
    }

    public List<Promocion> obtenerPromocionesPorProducto(Long productoId) {
        return promocionRepository.findByProductoIdAndActivoTrue(productoId);
    }

    public Map<String, Object> obtenerEstadisticasPromociones() {
        Map<String, Object> stats = memoriaEstadoService.getEstadisticas();
        
        // Agregar estadísticas específicas de promociones
        stats.put("promocionesActivas", listarPromocionesActivas().size());
        stats.put("totalPromociones", promocionRepository.count());
        stats.put("candidatosPendientes", 
            candidatoRepository.countByEstado("PENDIENTE"));
        
        return stats;
    }
}