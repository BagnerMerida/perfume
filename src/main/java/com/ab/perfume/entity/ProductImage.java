package com.ab.perfume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Producto al que pertenece la imagen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "main_image", nullable = false)
    private Boolean mainImage = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @PrePersist
    public void prePersist() {
        if (this.mainImage == null) this.mainImage = false;
        if (this.displayOrder == null) this.displayOrder = 0;
    }

}
