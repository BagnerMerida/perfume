package com.ab.perfume.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailResponseDTO {

    private Long productId;

    private String productName;

    private Long variantId;

    private String variantName;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;

}
