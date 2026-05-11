package com.ab.perfume.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductImageRequestDTO {

    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String imageUrl;

    private Boolean mainImage;

    private Integer displayOrder;

}
