package com.ab.perfume.entity;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Producto al que pertenece la variante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "size_ml")
    private Integer sizeMl;

    @Column(name = "variant_name", length = 100)
    private String variantName; // 50ml, 100ml, Set, Miniatura

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(length = 80, unique = true)
    private String sku;

    @Column(nullable = false)
    private Boolean active = true;

    @PrePersist
    public void prePersist() {

        if (this.stock == null) this.stock = 0;
        if (this.active == null) this.active = true;

    }

}
