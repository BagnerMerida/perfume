package com.ab.perfume.dto;

import com.ab.perfume.enums.Gender;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    @NotNull(message = "La marca es obligatoria")
    private Long brandId;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 180)
    private String name;

    private String description;

    @Size(max = 80)
    private String fragranceType;

    private Gender gender;

    @Size(max = 80)
    private String concentration;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private Boolean featured;

    private Boolean isNew;

    private Boolean bestSeller;

}
