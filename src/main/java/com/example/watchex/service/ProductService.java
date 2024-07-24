package com.example.watchex.service;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.RatingDto;
import com.example.watchex.dto.SearchDto;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.Rating;
import com.example.watchex.repository.ProductRepository;
import com.example.watchex.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<ProductDto> get(Map<String, String> params) {
        SearchDto dto = new SearchDto();
        if (params.get("page") != null) {
            dto.setPageIndex(Integer.parseInt(params.get("page")) - 1);
        }
        if (params.get("pageSize") != null) {
            dto.setPageSize(Integer.parseInt(params.get("pageSize")));
        }
        if (params.get("keyword") != null) {
            dto.setKeyword(params.get("keyword"));
        }
        Sort sort = Sort.by(
                Sort.Order.asc("pro_pay"),
                Sort.Order.desc("id"));
        if (params.get("sort") != null && Integer.parseInt(params.get("sort")) == 1) {
            sort = Sort.by(
                    Sort.Order.asc("pro_price"),
                    Sort.Order.desc("id"));
        }
        if (params.get("sort") != null && Integer.parseInt(params.get("sort")) == 2) {
            sort = Sort.by(
                    Sort.Order.desc("pro_price"),
                    Sort.Order.desc("id"));
        }
        if (params.get("price") != null) {
            dto.setPrice(Integer.parseInt(params.get("price")));
        }
        return productRepository.search(dto, PageRequest.of(dto.getPageIndex(), dto.getPageSize(), sort));
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
    public Page<ProductDto> findBySlugCategory(String slug, Map<String, String> params) {
        SearchDto dto = new SearchDto();
        if (params.get("page") != null) {
            dto.setPageIndex(Integer.parseInt(params.get("page")) - 1);
        }
        if (params.get("pageSize") != null) {
            dto.setPageSize(Integer.parseInt(params.get("pageSize")));
        }
        return productRepository.findBySlugCategory(slug, PageRequest.of(dto.getPageIndex(), dto.getPageSize(), Sort.by("id").descending()));
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
