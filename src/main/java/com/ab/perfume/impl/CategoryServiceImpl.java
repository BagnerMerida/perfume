package com.ab.perfume.impl;

import com.ab.perfume.dto.CategoryRequestDTO;
import com.ab.perfume.dto.CategoryResponseDTO;
import com.ab.perfume.entity.Category;
import com.ab.perfume.repository.CategoryRepository;
import com.ab.perfume.service.CategoryService;
import com.ab.perfume.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        String slug = SlugUtil.toSlug(request.getName());

        if (categoryRepository.existsBySlug(slug)) {
            throw new RuntimeException("Ya existe una categoría con ese slug");
        }

        Category parent = null;

        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .parent(parent)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return toResponse(categoryRepository.save(category));

    }

    @Override
    public List<CategoryResponseDTO> findAll() {

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return toResponse(category);

    }

    @Override
    public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoryRepository.findBySlug(SlugUtil.toSlug(request.getName()))
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {

                    throw new RuntimeException("Ya existe una categoría con ese slug");

                });

        String slug = SlugUtil.toSlug(request.getName());

        Category parent = null;

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new RuntimeException("Una categoría no puede ser padre de sí misma");
            }

            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
        }

        category.setName(request.getName());
        category.setSlug(slug);
        category.setParent(parent);

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        return toResponse(categoryRepository.save(category));

    }

    @Override
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoryRepository.delete(category);

    }

    private CategoryResponseDTO toResponse(Category category) {

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .build();

    }

}
