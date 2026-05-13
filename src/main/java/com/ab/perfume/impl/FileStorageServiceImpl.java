package com.ab.perfume.impl;

import com.ab.perfume.config.UploadProperties;
import com.ab.perfume.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final UploadProperties uploadProperties;

    @Override
    public String saveLocionImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("La imagen es obligatoria");
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            throw new RuntimeException("Nombre de archivo inválido");
        }

        String cleanName = cleanFileName(originalName);
        String finalName = UUID.randomUUID() + "-" + cleanName;

        try {
            Path directory = Paths.get(uploadProperties.getLocionesPath());

            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            Path targetPath = directory.resolve(finalName);
            Files.copy(

                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING

            );

            return finalName;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
        }

    }

    @Override
    public void deleteLocionImage(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            return;
        }

        try {
            Path filePath = Paths.get(uploadProperties.getLocionesPath())

                    .resolve(fileName);
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage());
        }
    }

    private String cleanFileName(String fileName) {

        String normalized = Normalizer.normalize(fileName, Normalizer.Form.NFD)

                .replaceAll("\\p{M}", "");
        return normalized
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9._-]", "")
                .replaceAll("-+", "-");
    }

}
