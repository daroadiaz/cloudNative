package com.example.ecommerce.repository;

import com.example.ecommerce.model.AlertaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaStockRepository extends JpaRepository<AlertaStock, Long> {
    List<AlertaStock> findByProductoIdAndEstado(Long productoId, String estado);
    List<AlertaStock> findByEstado(String estado);
    List<AlertaStock> findByTipoAlerta(String tipoAlerta);
}