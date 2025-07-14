package com.example.ecommerce.repository;

import com.example.ecommerce.model.ReglaPromocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReglaPromocionRepository extends JpaRepository<ReglaPromocion, Long> {
    List<ReglaPromocion> findByActivaTrue();
    List<ReglaPromocion> findByTipoRegla(String tipoRegla);
    List<ReglaPromocion> findByActivaTrueOrderByPrioridadDesc();
}