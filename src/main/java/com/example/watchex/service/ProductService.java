package com.example.watchex.service;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Product;
import com.example.watchex.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> get(int page) {
        return productRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }
    public void save(Product product) {
        productRepository.save(product);
    }

    public Product show(Integer id) throws ClassNotFoundException {
        Optional<Product> result = productRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ClassNotFoundException("Product not found");
    }

    public ProductDto findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public List<ProductDto> getProductsAccessoriess() {
        return productRepository.getProductsAccessoriess(PageRequest.of(0, 10, Sort.by("id").descending()));
    }

    public List<ProductDto> getProductsGlass() {
        return productRepository.getProductsGlass(PageRequest.of(0, 10, Sort.by("id").descending()));
    }

    public List<ProductDto> getProductsWatch() {
        return productRepository.getProductsWatch(PageRequest.of(0, 10, Sort.by("id").descending()));
    }

    public List<ProductDto> getProductsByCategory(Integer cate, Integer limit) {
        return productRepository.getProductsByCategory(cate, PageRequest.of(0, limit, Sort.by("id").descending()));
    }
}
