package com.example.watchex.service;

import com.example.watchex.entity.Product;
import com.example.watchex.entity.ProductImage;
import com.example.watchex.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository repository;

    public Page<ProductImage> get(int page) {
        return repository.findAll(PageRequest.of(page - 1, 10, Sort.by("id").descending()));
    }

    public List<ProductImage> getAll() {
        return repository.findAll();
    }

    public void save(ProductImage productimage) {
        repository.save(productimage);
    }

    public ProductImage show(Integer id) throws ClassNotFoundException {
        Optional<ProductImage> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ClassNotFoundException("ProductImage not found");
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<ProductImage> findByProduct(Product product) {
        return repository.findByProduct(product);
    }

}
