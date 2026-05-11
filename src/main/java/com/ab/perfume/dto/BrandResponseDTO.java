package com.ab.perfume.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BrandResponseDTO {

    private Long id;
    private String name;
    private String slug;
    private Boolean active;
    private LocalDateTime createdAt;

}
