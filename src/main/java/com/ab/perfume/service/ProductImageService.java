package com.ab.perfume.service;

import com.ab.perfume.dto.ProductImageRequestDTO;
import com.ab.perfume.dto.ProductImageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    ProductImageResponseDTO create(ProductImageRequestDTO request);

    List<ProductImageResponseDTO> findByProduct(Long productId);

    void delete(Long id);

    ProductImageResponseDTO uploadImage(
            Long productId,
            Boolean mainImage,
            Integer displayOrder,
            MultipartFile file
    );

    ProductImageResponseDTO updateImage(
            Long imageId,
            Boolean mainImage,
            Integer displayOrder,
            MultipartFile file
    );

}
