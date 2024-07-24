package com.example.watchex.repository;

import com.example.watchex.dto.RatingDto;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.Rating;
import com.example.watchex.entity.User;
import com.example.watchex.entity.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<UserFavourite, Integer> {
    @Query(nativeQuery = true, value =
            "select r_number, count(r_number) as total, count(r_number) as count_number from ratings " +
                    "where r_product_id = :productId group by r_number")
    List<RatingDto> getRatingProduct(Integer productId);


}
