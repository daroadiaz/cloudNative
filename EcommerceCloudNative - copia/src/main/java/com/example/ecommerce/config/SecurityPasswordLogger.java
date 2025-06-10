package com.example.ecommerce.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@EnableScheduling
public class SecurityPasswordLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityPasswordLogger.class);
    private String generatedPassword;
    
    @EventListener(ApplicationReadyEvent.class)
    public void logPasswordOnStartup() {
        // Spring Boot genera la contraseña internamente, pero podemos capturarla
        // revisando los logs o configurando nuestro propio usuario
        logger.info("==============================================");
        logger.info("APLICACIÓN INICIADA - CREDENCIALES DE ACCESO:");
        logger.info("Usuario: user");
        logger.info("Contraseña: Buscar 'Using generated security password' en los logs anteriores");
        logger.info("==============================================");
    }
    
    @Scheduled(fixedDelay = 30000) // Cada 30 segundos
    public void logPasswordPeriodically() {
        logger.info("==============================================");
        logger.info("RECORDATORIO - CREDENCIALES DE ACCESO:");
        logger.info("Usuario: user");
        logger.info("Contraseña: Buscar 'Using generated security password' en los logs");
        logger.info("==============================================");
    }
}