package com.ab.perfume.controller;

import com.ab.perfume.dto.ProductVariantRequestDTO;
import com.ab.perfume.dto.ProductVariantResponseDTO;
import com.ab.perfume.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/product-variants")
@RestController
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<ProductVariantResponseDTO> create(
            @Valid @RequestBody ProductVariantRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productVariantService.create(request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductVariantResponseDTO>> findByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(productVariantService.findByProduct(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequestDTO request
    ) {
        return ResponseEntity.ok(productVariantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        productVariantService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
