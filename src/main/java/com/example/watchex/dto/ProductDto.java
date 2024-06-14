package com.example.watchex.dto;

import com.example.watchex.entity.Category;
import com.example.watchex.entity.Keyword;

import java.util.Date;
import java.util.Set;

public interface ProductDto {
    Integer getId();

    String getPro_name();

    String getPro_avatar();

    String getPro_slug();

    Integer getPro_amount();

    Integer getPro_view();

    Integer getPro_price();

    Integer getPro_sale();

    Category getCategory();

    Integer getPro_favourite();

    Integer getPro_hot();

    Integer getPro_active();

    Integer getPro_admin_id();

    Integer getPro_pay();

    String getPro_description();

    String getPro_content();

    Integer getPro_review_total();

    String getKeywordseo();

    Integer getPro_review_star();

    String get_wysihtml5_mode();

    Set<Keyword> getKeyword();

    Date getCreatedAt();

    Date getUpdatedAt();

    default Integer getStar() {
        if (getPro_review_total() > 0) {
            return (getPro_review_star() - 5 ) / getPro_review_total();
        }
        return getPro_review_star();
    }
}
