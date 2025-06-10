package com.example.ecommerce.service;

import com.example.ecommerce.model.Instrumento;
import com.example.ecommerce.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoService {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    public List<Instrumento> getAllInstrumentos() {
        return instrumentoRepository.findAll();
    }

    public Optional<Instrumento> getInstrumentoById(Long id) {
        return instrumentoRepository.findById(id);
    }

    public Instrumento saveInstrumento(Instrumento instrumento) {
        return instrumentoRepository.save(instrumento);
    }

    public void deleteInstrumento(Long id) {
        instrumentoRepository.deleteById(id);
    }
}
