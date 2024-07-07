package com.example.watchex.repository;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.User;
import com.example.watchex.entity.UserFavourite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserFavouriteRepository extends JpaRepository<UserFavourite, Integer> {

    @Query("SELECT userFavourite FROM UserFavourite userFavourite " +
            "WHERE userFavourite.product.id = :productId and userFavourite.user = :user")
    UserFavourite getByProductId(User user, int productId);


}
