package com.example.ecommerce.controller;

import com.example.ecommerce.model.Instrumento;
import com.example.ecommerce.service.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrumentos")
@CrossOrigin(origins = "*")  // Para permitir solicitudes desde frontend (puedes limitar el origen)
public class InstrumentoController {

    @Autowired
    private InstrumentoService instrumentoService;

    @GetMapping
    public List<Instrumento> getAllInstrumentos() {
        return instrumentoService.getAllInstrumentos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instrumento> getInstrumentoById(@PathVariable Long id) {
        return instrumentoService.getInstrumentoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Instrumento createInstrumento(@RequestBody Instrumento instrumento) {
        return instrumentoService.saveInstrumento(instrumento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instrumento> updateInstrumento(@PathVariable Long id, @RequestBody Instrumento instrumentoDetalles) {
        return instrumentoService.getInstrumentoById(id)
                .map(instrumento -> {
                    instrumento.setNombre(instrumentoDetalles.getNombre());
                    instrumento.setMarca(instrumentoDetalles.getMarca());
                    instrumento.setTipo(instrumentoDetalles.getTipo());
                    instrumento.setPrecio(instrumentoDetalles.getPrecio());
                    instrumento.setStock(instrumentoDetalles.getStock());
                    Instrumento actualizado = instrumentoService.saveInstrumento(instrumento);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrumento(@PathVariable Long id) {
        if (instrumentoService.getInstrumentoById(id).isPresent()) {
            instrumentoService.deleteInstrumento(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

