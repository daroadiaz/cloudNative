package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "*")
public class MonitorController {

    @Value("${json.output.path}")
    private String jsonOutputPath;

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Ecommerce Consumer");
        response.put("timestamp", LocalDateTime.now());
        response.put("queues", Arrays.asList("sales-queue", "promotions-queue"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/files")
    public ResponseEntity<?> listGeneratedFiles() {
        try {
            Path outputDir = Paths.get(jsonOutputPath);
            List<Map<String, Object>> files = new ArrayList<>();
            
            if (Files.exists(outputDir)) {
                Files.list(outputDir)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        File file = path.toFile();
                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("name", file.getName());
                        fileInfo.put("size", file.length() + " bytes");
                        fileInfo.put("lastModified", new Date(file.lastModified()));
                        fileInfo.put("path", file.getAbsolutePath());
                        files.add(fileInfo);
                    });
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalFiles", files.size());
            response.put("outputPath", jsonOutputPath);
            response.put("files", files);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al listar archivos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("service", "Ecommerce Consumer");
        stats.put("uptime", "Running");
        stats.put("consumers", Map.of(
            "sales", "Active - Saving to Oracle Cloud",
            "promotions", "Active - Generating JSON files"
        ));
        stats.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(stats);
    }
}