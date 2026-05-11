package com.ab.perfume.service;

import com.ab.perfume.dto.BrandRequestDTO;
import com.ab.perfume.dto.BrandResponseDTO;

import java.util.List;

public interface BrandService {

    BrandResponseDTO create(BrandRequestDTO request);

    List<BrandResponseDTO> findAll();

    BrandResponseDTO findById(Long id);

    BrandResponseDTO update(Long id, BrandRequestDTO request);

    void delete(Long id);

}
