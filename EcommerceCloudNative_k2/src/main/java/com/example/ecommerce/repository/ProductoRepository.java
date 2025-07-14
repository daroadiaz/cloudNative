package com.example.ecommerce.repository;

import com.example.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockActualLessThanEqual(Integer stock);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoria(String categoria);
}