package com.example.watchex.repository;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p " +
            "WHERE p.pro_active = 1  " +
            "order by p.pro_pay desc")
    List<ProductDto> getProductsAccessoriess(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.pro_active = 1 " +
            "order by p.pro_pay desc")
    List<ProductDto> getProductsGlass(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.pro_active = 1 " +
            "order by p.pro_pay desc")
    List<ProductDto> getProductsWatch(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.pro_active = 1 " +
            "order by p.pro_pay desc")
    List<ProductDto> getProductsByCategory(Integer cate, Pageable pageable);

    @Query("select p from Product p where p.pro_slug = :slug")
    ProductDto findBySlug(String slug);
}
