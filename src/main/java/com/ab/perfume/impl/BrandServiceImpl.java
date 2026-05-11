package com.ab.perfume.impl;

import com.ab.perfume.dto.BrandRequestDTO;
import com.ab.perfume.dto.BrandResponseDTO;
import com.ab.perfume.entity.Brand;
import com.ab.perfume.repository.BrandRepository;
import com.ab.perfume.service.BrandService;
import com.ab.perfume.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public BrandResponseDTO create(BrandRequestDTO request) {

        if (brandRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Ya existe una marca con ese nombre");
        }

        String slug = SlugUtil.toSlug(request.getName());
        if (brandRepository.existsBySlug(slug)) {
            throw new RuntimeException("Ya existe una marca con ese slug");
        }

        Brand brand = Brand.builder()
                .name(request.getName())
                .slug(slug)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return toResponse(brandRepository.save(brand));

    }

    @Override
    public List<BrandResponseDTO> findAll() {

        return brandRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BrandResponseDTO findById(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        return toResponse(brand);

    }

    @Override
    public BrandResponseDTO update(Long id, BrandRequestDTO request) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        String slug = SlugUtil.toSlug(request.getName());
        brandRepository.findByNameIgnoreCase(request.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe una marca con ese nombre");
                });

        brandRepository.findBySlug(slug)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe una marca con ese slug");
                });

        brand.setName(request.getName());
        brand.setSlug(slug);

        if (request.getActive() != null) {
            brand.setActive(request.getActive());
        }

        return toResponse(brandRepository.save(brand));

    }

    @Override
    public void delete(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        brandRepository.delete(brand);

    }

    private BrandResponseDTO toResponse(Brand brand) {
        return BrandResponseDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .active(brand.getActive())
                .createdAt(brand.getCreatedAt())
                .build();
    }

}
