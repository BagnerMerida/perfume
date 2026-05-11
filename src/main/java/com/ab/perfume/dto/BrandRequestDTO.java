package com.ab.perfume.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class BrandRequestDTO {

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    private String name;

    private Boolean active;

}
