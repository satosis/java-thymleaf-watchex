package com.example.watchex.repository;

import com.example.watchex.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where c.c_slug = :slug")
    Category findBySlug(String slug);
}
