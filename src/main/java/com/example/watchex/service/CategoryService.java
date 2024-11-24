package com.example.watchex.service;

import com.example.watchex.entity.Category;
import com.example.watchex.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public Page<Category> get(int page) {
        return repository.findAll(PageRequest.of(page - 1, 10, Sort.by("id").descending()));
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public void save(Category category) {
        repository.save(category);
    }

    public Category show(Integer id) throws ClassNotFoundException {
        Optional<Category> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ClassNotFoundException("Category not found");
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
    public Category findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

}
