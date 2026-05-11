package com.ab.perfume.impl;

import com.ab.perfume.dto.ProductImageRequestDTO;
import com.ab.perfume.dto.ProductImageResponseDTO;
import com.ab.perfume.entity.Product;
import com.ab.perfume.entity.ProductImage;
import com.ab.perfume.repository.ProductImageRepository;
import com.ab.perfume.repository.ProductRepository;
import com.ab.perfume.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductRepository productRepository;

    @Override
    public ProductImageResponseDTO create(ProductImageRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Si viene como imagen principal,
        // quitamos la principal anterior
        if (Boolean.TRUE.equals(request.getMainImage())) {
            productImageRepository.findByProductIdAndMainImageTrue(product.getId())
                    .ifPresent(existing -> {
                        existing.setMainImage(false);
                        productImageRepository.save(existing);
                    });

        }

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(request.getImageUrl())
                .mainImage(request.getMainImage() != null ? request.getMainImage() : false)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        return toResponse(productImageRepository.save(image));

    }

    @Override
    public List<ProductImageResponseDTO> findByProduct(Long productId) {

        return productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        productImageRepository.delete(image);
    }

    private ProductImageResponseDTO toResponse(ProductImage image) {
        return ProductImageResponseDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .mainImage(image.getMainImage())
                .displayOrder(image.getDisplayOrder())
                .build();
    }

}
