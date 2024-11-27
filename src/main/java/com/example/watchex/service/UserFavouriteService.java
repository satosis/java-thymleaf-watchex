package com.example.watchex.service;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.User;
import com.example.watchex.entity.UserFavourite;
import com.example.watchex.repository.ProductRepository;
import com.example.watchex.repository.UserFavouriteRepository;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFavouriteService {
    @Autowired
    private UserFavouriteRepository userFavouriteRepository;

    public UserFavourite getByProductId(Product product) {
        User user = CommonUtils.getCurrentUser();
        UserFavourite result = userFavouriteRepository.getByProductId(user, product.getId());
        return result;
    }

}
