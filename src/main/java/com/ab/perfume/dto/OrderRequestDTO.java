package com.ab.perfume.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String customerName;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    private String address;

    @Valid
    @NotEmpty(message = "El pedido debe tener al menos un producto")
    private List<OrderItemRequestDTO> items;

}
