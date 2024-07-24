package com.example.watchex.dto;

import com.example.watchex.entity.Category;
import com.example.watchex.entity.Keyword;
import com.example.watchex.entity.Rating;

import java.util.Date;
import java.util.Set;

public interface RatingDto {

    Integer getR_Number();
    Integer getCount_number();
    Integer getTotal();

}
