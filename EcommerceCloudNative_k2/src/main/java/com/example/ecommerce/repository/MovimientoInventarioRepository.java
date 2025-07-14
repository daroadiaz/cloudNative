package com.example.ecommerce.repository;

import com.example.ecommerce.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByProductoId(Long productoId);
    List<MovimientoInventario> findByTipoMovimiento(String tipo);
    List<MovimientoInventario> findByFechaMovimientoBetween(LocalDateTime start, LocalDateTime end);
    List<MovimientoInventario> findAllByOrderByFechaMovimientoDesc();
}