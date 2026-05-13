package com.ab.perfume.service;

import com.ab.perfume.dto.ProductRequestDTO;
import com.ab.perfume.dto.ProductResponseDTO;
import com.ab.perfume.enums.Gender;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO request);

    List<ProductResponseDTO> findAll();

    ProductResponseDTO findById(Long id);

    ProductResponseDTO findBySlug(String slug);

    ProductResponseDTO update(Long id, ProductRequestDTO request);

    void delete(Long id);

    List<ProductResponseDTO> findByGender(Gender gender);

    List<ProductResponseDTO> findFeatured();

    List<ProductResponseDTO> findNewProducts();

    List<ProductResponseDTO> findBestSellers();

    List<ProductResponseDTO> filterProducts(
            String search,
            Gender gender,
            Long brandId,
            Long categoryId,
            Boolean featured,
            Boolean isNew,
            Boolean bestSeller
    );

    ProductResponseDTO getBySlug(String slug);

}
