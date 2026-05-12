package com.ab.perfume.service;

import com.ab.perfume.dto.OrderRequestDTO;
import com.ab.perfume.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO create(OrderRequestDTO request);

    List<OrderResponseDTO> findAll();

    OrderResponseDTO findById(Long id);

}
