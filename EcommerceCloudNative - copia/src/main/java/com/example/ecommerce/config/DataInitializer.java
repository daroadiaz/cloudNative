package com.example.ecommerce.config;

import com.example.ecommerce.model.Instrumento;
import com.example.ecommerce.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Bean
    CommandLineRunner init() {
        return args -> {
            // Verificar si ya hay datos
            if (instrumentoRepository.count() == 0) {
                // Guitarras
                instrumentoRepository.save(new Instrumento("Stratocaster", "Fender", "Guitarra Eléctrica", 1500.00, 10));
                instrumentoRepository.save(new Instrumento("Les Paul", "Gibson", "Guitarra Eléctrica", 2500.00, 5));
                instrumentoRepository.save(new Instrumento("Clásica C40", "Yamaha", "Guitarra Clásica", 300.00, 15));
                
                // Bajos
                instrumentoRepository.save(new Instrumento("Precision Bass", "Fender", "Bajo Eléctrico", 1800.00, 7));
                instrumentoRepository.save(new Instrumento("Thunderbird", "Gibson", "Bajo Eléctrico", 2200.00, 3));
                
                // Teclados
                instrumentoRepository.save(new Instrumento("PSR-E373", "Yamaha", "Teclado", 400.00, 12));
                instrumentoRepository.save(new Instrumento("Privia PX-160", "Casio", "Piano Digital", 800.00, 6));
                
                // Baterías
                instrumentoRepository.save(new Instrumento("Stage Custom", "Yamaha", "Batería Acústica", 1200.00, 4));
                instrumentoRepository.save(new Instrumento("Roadshow", "Pearl", "Batería Acústica", 900.00, 5));
                
                // Vientos
                instrumentoRepository.save(new Instrumento("YAS-280", "Yamaha", "Saxofón Alto", 1100.00, 8));
                instrumentoRepository.save(new Instrumento("TR300H2", "Bach", "Trompeta", 600.00, 10));
                
                System.out.println("Datos de prueba inicializados correctamente");
            }
        };
    }
}