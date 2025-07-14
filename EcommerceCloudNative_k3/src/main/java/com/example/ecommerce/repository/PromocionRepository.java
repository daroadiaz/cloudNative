package com.example.ecommerce.repository;

import com.example.ecommerce.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    List<Promocion> findByActivoTrue();
    List<Promocion> findByActivoTrueAndFechaFinAfter(LocalDateTime fecha);
    List<Promocion> findByProductoIdAndActivoTrue(Long productoId);
    List<Promocion> findByCategoria(String categoria);
}