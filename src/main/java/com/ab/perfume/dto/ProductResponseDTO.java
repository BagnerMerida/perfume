package com.ab.perfume.dto;

import com.ab.perfume.enums.Gender;
import com.ab.perfume.enums.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data

@Builder
public class ProductResponseDTO {

    private Long id;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String slug;
    private String description;
    private String fragranceType;
    private Gender gender;
    private String concentration;
    private BigDecimal price;
    private Integer stock;
    private ProductStatus status;
    private Boolean featured;
    private Boolean isNew;
    private Boolean bestSeller;
    private String mainImageUrl;
    private List<ProductImageResponseDTO> images;
    private List<ProductVariantResponseDTO> variants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
