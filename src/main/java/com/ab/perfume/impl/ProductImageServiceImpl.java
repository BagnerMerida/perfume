package com.ab.perfume.impl;

import com.ab.perfume.config.UploadProperties;
import com.ab.perfume.dto.ProductImageRequestDTO;
import com.ab.perfume.dto.ProductImageResponseDTO;
import com.ab.perfume.entity.Product;
import com.ab.perfume.entity.ProductImage;
import com.ab.perfume.exception.ResourceNotFoundException;
import com.ab.perfume.repository.ProductImageRepository;
import com.ab.perfume.repository.ProductRepository;
import com.ab.perfume.service.FileStorageService;
import com.ab.perfume.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductRepository productRepository;

    private final FileStorageService fileStorageService;

    private final UploadProperties uploadProperties;

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
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));
        fileStorageService.deleteLocionImage(image.getImageUrl());

        productImageRepository.delete(image);
    }

    private ProductImageResponseDTO toResponse(ProductImage image) {
        return ProductImageResponseDTO.builder()
                .id(image.getId())
                .imageUrl(uploadProperties.getLocionesUrl() + "/" + image.getImageUrl())
                .mainImage(image.getMainImage())
                .displayOrder(image.getDisplayOrder())
                .build();
    }

    @Override
    public ProductImageResponseDTO uploadImage(
            Long productId,
            Boolean mainImage,
            Integer displayOrder,
            MultipartFile file
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (Boolean.TRUE.equals(mainImage)) {
            productImageRepository.findByProductIdAndMainImageTrue(productId)
                    .ifPresent(existing -> {
                        existing.setMainImage(false);
                        productImageRepository.save(existing);
                    });
        }

        String fileName = fileStorageService.saveLocionImage(file);

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(fileName)
                .mainImage(mainImage != null ? mainImage : false)
                .displayOrder(displayOrder != null ? displayOrder : 0)
                .build();

        return toResponse(productImageRepository.save(image));
    }

    @Override
    public ProductImageResponseDTO updateImage(
            Long imageId,
            Boolean mainImage,
            Integer displayOrder,
            MultipartFile file
    ) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));

        if (file != null && !file.isEmpty()) {
            fileStorageService.deleteLocionImage(image.getImageUrl());
            String newFileName = fileStorageService.saveLocionImage(file);
            image.setImageUrl(newFileName);
        }

        if (mainImage != null) {

            if (mainImage) {
                productImageRepository
                        .findByProductIdAndMainImageTrue(image.getProduct().getId())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(image.getId())) {
                                existing.setMainImage(false);
                                productImageRepository.save(existing);
                            }
                        });
            }

            image.setMainImage(mainImage);
        }

        if (displayOrder != null) {
            image.setDisplayOrder(displayOrder);
        }

        return toResponse(productImageRepository.save(image));
    }

}
