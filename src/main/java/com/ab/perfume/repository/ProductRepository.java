package com.ab.perfume.repository;

import com.ab.perfume.entity.Product;
import com.ab.perfume.enums.Gender;
import com.ab.perfume.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByGenderAndStatus(Gender gender, ProductStatus status);

    List<Product> findByBrandIdAndStatus(Long brandId, ProductStatus status);

    List<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status);

    List<Product> findByFeaturedTrueAndStatus(ProductStatus status);

    List<Product> findByIsNewTrueAndStatus(ProductStatus status);

    List<Product> findByBestSellerTrueAndStatus(ProductStatus status);

    @Query("""
    SELECT p FROM Product p
    WHERE p.status = :status
    AND (
        :search = ''
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    AND (:gender IS NULL OR p.gender = :gender)
    AND (:brandId IS NULL OR p.brand.id = :brandId)
    AND (:categoryId IS NULL OR p.category.id = :categoryId)
    AND (:featured IS NULL OR p.featured = :featured)
    AND (:isNew IS NULL OR p.isNew = :isNew)
    AND (:bestSeller IS NULL OR p.bestSeller = :bestSeller)
""")
    List<Product> filterProducts(
            @Param("status") ProductStatus status,
            @Param("search") String search,
            @Param("gender") Gender gender,
            @Param("brandId") Long brandId,
            @Param("categoryId") Long categoryId,
            @Param("featured") Boolean featured,
            @Param("isNew") Boolean isNew,
            @Param("bestSeller") Boolean bestSeller
    );

    Optional<Product> findBySlugAndStatus(String slug, ProductStatus status);

}
