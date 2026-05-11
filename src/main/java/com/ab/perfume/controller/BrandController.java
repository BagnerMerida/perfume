package com.ab.perfume.controller;

import com.ab.perfume.dto.BrandRequestDTO;
import com.ab.perfume.dto.BrandResponseDTO;
import com.ab.perfume.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/brands")
@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponseDTO> create(
            @Valid @RequestBody BrandRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(brandService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequestDTO request
    ) {
        return ResponseEntity.ok(brandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
