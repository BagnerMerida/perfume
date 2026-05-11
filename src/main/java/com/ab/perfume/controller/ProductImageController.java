package com.ab.perfume.controller;


import com.ab.perfume.dto.ProductImageRequestDTO;
import com.ab.perfume.dto.ProductImageResponseDTO;
import com.ab.perfume.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/product-images")
@RestController
@RequiredArgsConstructor
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
}
