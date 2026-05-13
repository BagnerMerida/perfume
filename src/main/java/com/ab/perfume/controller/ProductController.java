package com.ab.perfume.controller;

import com.ab.perfume.dto.ProductRequestDTO;
import com.ab.perfume.dto.ProductResponseDTO;
import com.ab.perfume.enums.Gender;
import com.ab.perfume.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @PostMapping

    public ResponseEntity<ProductResponseDTO> create(
            @Valid @RequestBody ProductRequestDTO request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductResponseDTO> findBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(productService.findBySlug(slug));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request
    ) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<ProductResponseDTO>> findByGender(
            @PathVariable Gender gender
    ) {
        return ResponseEntity.ok(productService.findByGender(gender));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponseDTO>> findFeatured() {
        return ResponseEntity.ok(productService.findFeatured());
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponseDTO>> findNewProducts() {
        return ResponseEntity.ok(productService.findNewProducts());
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<List<ProductResponseDTO>> findBestSellers() {
        return ResponseEntity.ok(productService.findBestSellers());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponseDTO>> filterProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean isNew,
            @RequestParam(required = false) Boolean bestSeller
    ) {

        return ResponseEntity.ok(
                productService.filterProducts(
                        search,
                        gender,
                        brandId,
                        categoryId,
                        featured,
                        isNew,
                        bestSeller
                )
        );
    }

}
