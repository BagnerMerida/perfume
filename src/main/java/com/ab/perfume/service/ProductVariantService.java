package com.ab.perfume.service;

import com.ab.perfume.dto.ProductVariantRequestDTO;
import com.ab.perfume.dto.ProductVariantResponseDTO;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponseDTO create(ProductVariantRequestDTO request);

    List<ProductVariantResponseDTO> findByProduct(Long productId);

    ProductVariantResponseDTO update(Long id, ProductVariantRequestDTO request);

    void delete(Long id);

}
