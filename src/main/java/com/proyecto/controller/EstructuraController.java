
package com.proyecto.controller;

import com.proyecto.model.Operacion;
import com.proyecto.service.EstructuraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estructuras")
public class EstructuraController {

    @Autowired
    private EstructuraService estructuraService;

    @PostMapping("/cargar")
    public ResponseEntity<Map<String, Object>> cargarArchivo(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "El archivo debe ser de tipo CSV"));
            }
            List<String> imagenes = estructuraService.procesarArchivo(file);
            return ResponseEntity.ok(Map.of("mensaje", "Archivo procesado correctamente", "imagenes", imagenes));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al procesar el archivo: " + e.getMessage()));
        }
    }
}