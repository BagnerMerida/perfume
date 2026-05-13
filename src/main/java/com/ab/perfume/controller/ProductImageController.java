package com.ab.perfume.controller;


import com.ab.perfume.dto.ProductImageRequestDTO;
import com.ab.perfume.dto.ProductImageResponseDTO;
import com.ab.perfume.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/product-images")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageResponseDTO> create(
            @Valid @RequestBody ProductImageRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productImageService.create(request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageResponseDTO>> findByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(productImageService.findByProduct(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        productImageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductImageResponseDTO> uploadImage(
            @RequestParam Long productId,
            @RequestParam(required = false) Boolean mainImage,
            @RequestParam(required = false) Integer displayOrder,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productImageService.uploadImage(
                        productId,
                        mainImage,
                        displayOrder,
                        file
                ));
    }
}
