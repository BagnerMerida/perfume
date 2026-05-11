package com.ab.perfume.service;

import com.ab.perfume.dto.CategoryRequestDTO;
import com.ab.perfume.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO create(CategoryRequestDTO request);

    List<CategoryResponseDTO> findAll();

    CategoryResponseDTO findById(Long id);

    CategoryResponseDTO update(Long id, CategoryRequestDTO request);

    void delete(Long id);

}
