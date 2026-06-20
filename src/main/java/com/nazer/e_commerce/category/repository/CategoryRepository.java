package com.nazer.e_commerce.category.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nazer.e_commerce.category.schema.Category;
import java.util.List;
import org.bson.types.ObjectId;


@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);
    Optional<Category> findById(ObjectId id);
}
