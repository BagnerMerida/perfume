package com.ab.perfume.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductVariantResponseDTO {

    private Long id;
    private Integer sizeMl;
    private String variantName;
    private BigDecimal price;
    private Integer stock;
    private String sku;
    private Boolean active;

}
