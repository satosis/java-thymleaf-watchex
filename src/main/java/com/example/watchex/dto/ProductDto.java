package com.example.watchex.dto;

import com.example.watchex.entity.Category;
import com.example.watchex.entity.Keyword;
import com.example.watchex.entity.ProductImage;
import com.example.watchex.entity.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer id;

    private String pro_name;

    private String pro_avatar;

    private String pro_slug;

    private Integer pro_amount;

    private Integer pro_view;

    private Integer pro_price;

    private Integer pro_sale;

    private Category category;

    private Integer pro_favourite;

    private Integer pro_hot;

    private Integer pro_active;

    private Integer pro_admin_id;

    private Integer pro_pay;

    private String pro_description;

    private String pro_content;

    private Integer pro_review_total;

    private String Keywordseo;

    private Integer pro_review_star;

    private String _wysihtml5_mode;

    private Set<Keyword> keywords;
    private Set<ProductImage> productImages;

    private Set<Rating> getRatings;

    private Date CreatedAt;

    private Date UpdatedAt;

    Integer getStar() {
        if (pro_review_star > 0) {
            return (pro_review_star - 5) / pro_review_total;
        }
        return pro_review_star;
    }


}
