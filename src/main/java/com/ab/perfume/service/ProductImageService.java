package com.ab.perfume.service;

import com.ab.perfume.dto.ProductImageRequestDTO;
import com.ab.perfume.dto.ProductImageResponseDTO;

import java.util.List;

public interface ProductImageService {

    ProductImageResponseDTO create(ProductImageRequestDTO request);

    List<ProductImageResponseDTO> findByProduct(Long productId);

    void delete(Long id);

}
