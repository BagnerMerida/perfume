package com.ab.perfume.repository;

import com.ab.perfume.entity.Product;
import com.ab.perfume.enums.Gender;
import com.ab.perfume.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
