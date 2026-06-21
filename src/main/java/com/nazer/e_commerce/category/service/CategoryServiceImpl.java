package com.nazer.e_commerce.category.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nazer.e_commerce.category.Dto.CategoryDto;
import com.nazer.e_commerce.category.Dto.CategoryResponse;
import com.nazer.e_commerce.category.Dto.CategoryResponse.AddedBy;
import com.nazer.e_commerce.category.repository.CategoryRepository;
import com.nazer.e_commerce.category.schema.Category;
import com.nazer.e_commerce.common.global.PaginatedResponse;
import com.nazer.e_commerce.common.global.PaginatedResponse.Pagination;
import com.nazer.e_commerce.users.enums.UserRoles;
import com.nazer.e_commerce.users.repository.UserRepository;
import com.nazer.e_commerce.users.schema.User;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Category create(CategoryDto dto) {
        String slug = generateSlug(dto.getCategoryName());

        if (categoryRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category already exists");
        }

        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Category category = Category.builder()
                .categoryName(dto.getCategoryName())
                .slug(slug)
                .createdBy(currentUser.getId())
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public Category update(String id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        String slug = generateSlug(dto.getCategoryName());

        if (!slug.equals(category.getSlug()) && categoryRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "slug already exists");
        }

        category.setCategoryName(dto.getCategoryName());
        category.setSlug(slug);

        return categoryRepository.save(category);
    }

    @Override
    public CategoryResponse getBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        CategoryResponse.CategoryResponseBuilder builder = CategoryResponse.builder()
                .id(category.get_id())
                .categoryName(category.getCategoryName())
                .slug(category.getSlug())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user
                && user.getRole() == UserRoles.ADMIN
                && category.getCreatedBy() != null) {
            userRepository.findById(category.getCreatedBy().toHexString())
                    .ifPresent(creator -> builder.addedBy(
                            AddedBy.builder().name(creator.getFullName()).build()));
        }

        return builder.build();
    }

    @Override
    public PaginatedResponse<Category> getAll(int page, int size, String search) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            String regex = search.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
            criteriaList.add(new Criteria().orOperator(
                Criteria.where("categoryName").regex(regex, "i"),
                Criteria.where("slug").regex(regex, "i")
            ));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Category.class);

        PageRequest pageable = PageRequest.of(page, size);
        query.with(pageable);

        List<Category> categories = mongoTemplate.find(query, Category.class);

        Pagination pagination = Pagination.builder()
            .total(total)
            .totalPages((int) Math.ceil((double) total / size))
            .page(page)
            .count(size)
            .build();

        return PaginatedResponse.<Category>builder()
            .data(categories)
            .pagination(pagination)
            .build();
    }

    @Override
    public CategoryResponse getById(ObjectId id) {
        Category category = categoryRepository.findById(id)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND , "category not found"));
        
        CategoryResponse.CategoryResponseBuilder builder = CategoryResponse.builder()
                .id(category.get_id())
                .categoryName(category.getCategoryName())
                .slug(category.getSlug())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user
                && user.getRole() == UserRoles.ADMIN
                && category.getCreatedBy() != null) {
            userRepository.findById(category.getCreatedBy().toHexString())
                    .ifPresent(creator -> builder.addedBy(
                            AddedBy.builder().name(creator.getFullName()).build()));
        }

        return builder.build();

    }
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
