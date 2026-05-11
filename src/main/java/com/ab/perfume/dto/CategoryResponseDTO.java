package com.ab.perfume.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponseDTO {

    private Long id;

    private String name;

    private String slug;

    private Long parentId;

    private String parentName;

    private Boolean active;

    private LocalDateTime createdAt;

}
