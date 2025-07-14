package com.example.ecommerce.repository;

import com.example.ecommerce.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteEmail(String email);
    List<Venta> findByFechaVentaBetween(LocalDateTime start, LocalDateTime end);
    List<Venta> findByEstado(String estado);
}