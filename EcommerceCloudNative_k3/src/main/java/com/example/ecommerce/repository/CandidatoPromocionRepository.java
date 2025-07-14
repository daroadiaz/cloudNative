package com.example.ecommerce.repository;

import com.example.ecommerce.model.CandidatoPromocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoPromocionRepository extends JpaRepository<CandidatoPromocion, Long> {
    List<CandidatoPromocion> findByEstado(String estado);
    List<CandidatoPromocion> findByProductoId(Long productoId);
    Integer countByEstado(String estado);
}