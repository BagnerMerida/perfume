package com.ab.perfume.impl;

import com.ab.perfume.dto.ProductVariantRequestDTO;
import com.ab.perfume.dto.ProductVariantResponseDTO;
import com.ab.perfume.entity.Product;
import com.ab.perfume.entity.ProductVariant;
import com.ab.perfume.repository.ProductRepository;
import com.ab.perfume.repository.ProductVariantRepository;
import com.ab.perfume.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    private final ProductRepository productRepository;

    @Override
    public ProductVariantResponseDTO create(ProductVariantRequestDTO request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (request.getSku() != null && productVariantRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("Ya existe una variante con ese SKU");
        }

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .sizeMl(request.getSizeMl())
                .variantName(request.getVariantName())
                .price(request.getPrice())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .sku(request.getSku())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return toResponse(productVariantRepository.save(variant));
    }

    @Override
    public List<ProductVariantResponseDTO> findByProduct(Long productId) {

        return productVariantRepository.findByProductIdOrderBySizeMlAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductVariantResponseDTO update(Long id, ProductVariantRequestDTO request) {

        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (request.getSku() != null) {
            productVariantRepository.findBySku(request.getSku())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new RuntimeException("Ya existe una variante con ese SKU");
                    });
        }

        variant.setProduct(product);
        variant.setSizeMl(request.getSizeMl());
        variant.setVariantName(request.getVariantName());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock() != null ? request.getStock() : 0);
        variant.setSku(request.getSku());
        variant.setActive(request.getActive() != null ? request.getActive() : true);

        return toResponse(productVariantRepository.save(variant));
    }

    @Override
    public void delete(Long id) {
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
        productVariantRepository.delete(variant);
    }

    private ProductVariantResponseDTO toResponse(ProductVariant variant) {

        return ProductVariantResponseDTO.builder()
                .id(variant.getId())
                .sizeMl(variant.getSizeMl())
                .variantName(variant.getVariantName())
                .price(variant.getPrice())
                .stock(variant.getStock())
                .sku(variant.getSku())
                .active(variant.getActive())
                .build();
    }

}
