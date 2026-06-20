package com.nazer.e_commerce.category;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nazer.e_commerce.category.Dto.CategoryDto;
import com.nazer.e_commerce.category.Dto.CategoryResponse;
import com.nazer.e_commerce.category.schema.Category;
import com.nazer.e_commerce.category.service.CategoryService;
import com.nazer.e_commerce.common.global.PaginatedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole(T(com.nazer.e_commerce.users.enums.UserRoles).ADMIN.name())")
    public Category create(@RequestBody @Valid CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole(T(com.nazer.e_commerce.users.enums.UserRoles).ADMIN.name())")
    public Category update(@PathVariable String id, @RequestBody @Valid CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @GetMapping("/{slug}")
    public CategoryResponse getBySlug(@PathVariable String slug) {
        return categoryService.getBySlug(slug);
    }

    @GetMapping
    public PaginatedResponse<Category> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        return categoryService.getAll(page, size, search);
    }
}
