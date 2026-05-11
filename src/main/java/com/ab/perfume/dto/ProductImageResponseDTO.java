package com.ab.perfume.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageResponseDTO {

    private Long id;
    private String imageUrl;
    private Boolean mainImage;
    private Integer displayOrder;

}
