package com.example.watchex.repository;

import com.example.watchex.entity.Product;
import com.example.watchex.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query("select c from ProductImage c where c.product_id = :product")
    List<ProductImage> findByProduct(Product product);
}
