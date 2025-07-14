package com.example.ecommerce.repository;

import com.example.ecommerce.model.AnalisisVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalisisVentasRepository extends JpaRepository<AnalisisVentas, Long> {
    Optional<AnalisisVentas> findByProductoId(Long productoId);
    List<AnalisisVentas> findByVelocidadRotacion(String velocidadRotacion);
    List<AnalisisVentas> findByDiasSinVentaGreaterThan(Integer dias);
}