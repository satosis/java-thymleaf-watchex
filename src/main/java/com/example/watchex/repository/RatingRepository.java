package com.example.watchex.repository;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.RatingDto;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.Rating;
import com.example.watchex.entity.User;
import com.example.watchex.entity.UserFavourite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query(nativeQuery = true, value =
            "select r_number, count(r_number) as total, count(r_number) as count_number from ratings " +
                    "where r_product_id = :productId group by r_number")
    List<RatingDto> getRatingProduct(Integer productId);

    @Query("SELECT p FROM Rating p  where p.product = :product and p.r_user_id = :user order by  p.id desc")
    List<Rating> getMyRatingProduct(Product product, User user);

    @Query("SELECT p FROM Rating p  where p.product = :product order by  p.id desc")
    List<Rating> getListRatingProduct(Product product);

}
