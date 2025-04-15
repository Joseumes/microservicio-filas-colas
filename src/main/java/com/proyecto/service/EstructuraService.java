package com.proyecto.service;

import com.proyecto.model.Operacion;
import com.proyecto.repository.OperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class EstructuraService {

    @Autowired
    private OperacionRepository operacionRepository;

    public List<String> procesarArchivo(MultipartFile file) throws IOException {
        List<String> imagenes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                String estructura = partes[0];
                String operacion = partes[1];
                String valor = partes.length > 2 ? partes[2] : null;

                // Ejecutar la operación y generar la imagen
                String imagen = ejecutarOperacion(estructura, operacion, valor);
                imagenes.add(imagen);

                // Guardar la operación en MongoDB
                Operacion nuevaOperacion = new Operacion();
                nuevaOperacion.setEstructura(estructura);
                nuevaOperacion.setOperacion(operacion);
                nuevaOperacion.setValor(valor);
                nuevaOperacion.setImagen(imagen);
                operacionRepository.save(nuevaOperacion);
            }
        }
        return imagenes;
    }

    private String ejecutarOperacion(String estructura, String operacion, String valor) {
        // Generar un script DOT para Graphviz
        String dotScript = generarDotScript(estructura, operacion, valor);
        return generarImagen(dotScript);
    }

    private String generarDotScript(String estructura, String operacion, String valor) {
        // Ejemplo básico de un gráfico DOT
        return "digraph G { node [shape=record]; A -> B; }";
    }

    private String generarImagen(String dotScript) {
        String outputImagePath = "/tmp/output.png";
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"dot", "-Tpng", "-o", outputImagePath});
            process.getOutputStream().write(dotScript.getBytes());
            process.getOutputStream().close();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la imagen con Graphviz", e);
        }
        return outputImagePath; // Devuelve la ruta de la imagen generada
    }
}
