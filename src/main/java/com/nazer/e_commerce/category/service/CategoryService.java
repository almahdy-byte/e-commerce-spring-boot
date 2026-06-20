package com.nazer.e_commerce.category.service;

import org.bson.types.ObjectId;

import com.nazer.e_commerce.category.Dto.CategoryDto;
import com.nazer.e_commerce.category.Dto.CategoryResponse;
import com.nazer.e_commerce.category.schema.Category;
import com.nazer.e_commerce.common.global.PaginatedResponse;

public interface CategoryService {
    Category create(CategoryDto dto);
    Category update(String id, CategoryDto dto);
    CategoryResponse getBySlug(String slug);
    PaginatedResponse<Category> getAll(int page, int size, String search);
    CategoryResponse getById(ObjectId id);
}
