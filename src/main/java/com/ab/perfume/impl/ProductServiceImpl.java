package com.ab.perfume.impl;

import com.ab.perfume.config.UploadProperties;
import com.ab.perfume.dto.ProductRequestDTO;
import com.ab.perfume.dto.ProductResponseDTO;
import com.ab.perfume.entity.Brand;
import com.ab.perfume.entity.Category;
import com.ab.perfume.entity.Product;
import com.ab.perfume.enums.Gender;
import com.ab.perfume.enums.ProductStatus;
import com.ab.perfume.repository.BrandRepository;
import com.ab.perfume.repository.CategoryRepository;
import com.ab.perfume.repository.ProductImageRepository;
import com.ab.perfume.repository.ProductRepository;
import com.ab.perfume.service.ProductService;
import com.ab.perfume.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    private final ProductImageRepository productImageRepository;

    private final UploadProperties uploadProperties;

    @Override
    public ProductResponseDTO create(ProductRequestDTO request) {
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        String slug = SlugUtil.toSlug(request.getName());

        if (productRepository.existsBySlug(slug)) {
            throw new RuntimeException("Ya existe un producto con ese nombre");
        }

        Product product = Product.builder()
                .brand(brand)
                .category(category)
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .fragranceType(request.getFragranceType())
                .gender(request.getGender())
                .concentration(request.getConcentration())
                .price(request.getPrice())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .status(ProductStatus.ACTIVE)
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .isNew(request.getIsNew() != null ? request.getIsNew() : false)
                .bestSeller(request.getBestSeller() != null ? request.getBestSeller() : false)
                .build();

        return toResponse(productRepository.save(product));

    }

    @Override
    public List<ProductResponseDTO> findAll() {

        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDTO findById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toResponse(product);

    }

    @Override
    public ProductResponseDTO findBySlug(String slug) {

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toResponse(product);

    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        String slug = SlugUtil.toSlug(request.getName());

        productRepository.findBySlug(slug)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe un producto con ese nombre");
                });

        product.setBrand(brand);
        product.setCategory(category);
        product.setName(request.getName());
        product.setSlug(slug);
        product.setDescription(request.getDescription());
        product.setFragranceType(request.getFragranceType());
        product.setGender(request.getGender());
        product.setConcentration(request.getConcentration());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock() != null ? request.getStock() : 0);
        product.setFeatured(request.getFeatured() != null ? request.getFeatured() : false);
        product.setIsNew(request.getIsNew() != null ? request.getIsNew() : false);
        product.setBestSeller(request.getBestSeller() != null ? request.getBestSeller() : false);

        return toResponse(productRepository.save(product));

    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponseDTO> findByGender(Gender gender) {

        return productRepository.findByGenderAndStatus(gender, ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> findFeatured() {
        return productRepository.findByFeaturedTrueAndStatus(ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    @Override
    public List<ProductResponseDTO> findNewProducts() {
        return productRepository.findByIsNewTrueAndStatus(ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> findBestSellers() {
        return productRepository.findByBestSellerTrueAndStatus(ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponseDTO toResponse(Product product) {
        String mainImageUrl = productImageRepository
                .findByProductIdAndMainImageTrue(product.getId())
                .map(image -> uploadProperties.getLocionesUrl() + "/" + image.getImageUrl())
                .orElse(null);

        return ProductResponseDTO.builder()
                .id(product.getId())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .fragranceType(product.getFragranceType())
                .gender(product.getGender())
                .concentration(product.getConcentration())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .featured(product.getFeatured())
                .isNew(product.getIsNew())
                .bestSeller(product.getBestSeller())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .viewsCount(product.getViewsCount())
                .searchCount(product.getSearchCount())
                .salesCount(product.getSalesCount())
                .cartCount(product.getCartCount())
                .mainImageUrl(mainImageUrl)
                .build();

    }

    @Override
    public List<ProductResponseDTO> filterProducts(
            String search,
            Gender gender,
            Long brandId,
            Long categoryId,
            Boolean featured,
            Boolean isNew,
            Boolean bestSeller
    ) {

        String searchValue = search != null ? search.trim() : "";

        return productRepository.filterProducts(
                        ProductStatus.ACTIVE,
                        searchValue,
                        gender,
                        brandId,
                        categoryId,
                        featured,
                        isNew,
                        bestSeller
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    @Override
    public ProductResponseDTO getBySlug(String slug) {

        Product product = productRepository
                .findBySlugAndStatus(slug, ProductStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setViewsCount(product.getViewsCount() + 1);

        productRepository.save(product);

        return toResponse(product);
    }

}
