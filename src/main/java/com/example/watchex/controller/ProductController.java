package com.example.watchex.controller;

import com.example.watchex.dto.ProductDetailDto;
import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.RatingDto;
import com.example.watchex.entity.*;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.repository.RatingRepository;
import com.example.watchex.response.RatingResponse;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.UserFavouriteService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserFavouriteService userFavouriteService;

    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping("")
    public String index(@RequestParam Map<String, String> params, Model model) {
        List<Category> categories = categoryService.getAll();
        Page<ProductDto> products = productService.get(params);
        model.addAttribute("cartCount", Cart.cart.size());
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", params.get("page") != null ? Integer.parseInt(params.get("page")) : 1);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("price", params.get("price") != null ? params.get("price") : 0);
        model.addAttribute("sort", params.get("sort") != null ? params.get("sort") : 0);
        model.addAttribute("title", "Kết quả tìm kiếm cho " + params.get("keyword"));
        model.addAttribute("models", "/product?keyword=" + params.get("keyword") );
        return "product/list";
    }
    @GetMapping("/{slug}")
    public String detail(@PathVariable("slug") String slug, Model model) throws ClassNotFoundException {
        User user = CommonUtils.getCurrentUser();
        List<Category> categories = categoryService.getAll();
        ProductDetailDto product = productService.findBySlug(slug);
        Product entityProduct = productService.show(product.getId());
        List<RatingDto> ratings = ratingRepository.getRatingProduct(product.getId());
        List<Rating> listRating = ratingRepository.getListRatingProduct(entityProduct);
        List<Rating> myRating = ratingRepository.getMyRatingProduct(entityProduct, user);
        List<Product> productSuggest = productService.getProductsByCategory(product.getCategory().getId(), 3);
        UserFavourite userFavourite = userFavouriteService.getByProductId(entityProduct);
        String[] tags= product.getKeywordseo().split(",");
        model.addAttribute("cartCount", Cart.cart.size());
        model.addAttribute("product", product);
        model.addAttribute("ratings", ratings);
        model.addAttribute("categories", categories);
        model.addAttribute("productSuggest", productSuggest);
        model.addAttribute("userFavourite", userFavourite);
        model.addAttribute("myRating", myRating);
        model.addAttribute("listRating", listRating);
        model.addAttribute("tags", tags);
        return "product/detail";
    }

    @GetMapping("/category/{slug}")
    public String category(@PathVariable("slug") String slug, @RequestParam Map<String, String> params, Model model) {
        List<Category> categories = categoryService.getAll();
        Category category = categoryService.findBySlug(slug);
        Page<Product> products = productService.findBySlugCategory(slug, params);
        model.addAttribute("cartCount", Cart.cart.size());
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", params.get("page") != null ? Integer.parseInt(params.get("page")) : 1);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("price", params.get("price") != null ? params.get("price") : 0);
        model.addAttribute("sort", params.get("sort") != null ? params.get("sort") : 0);
        model.addAttribute("sort", params.get("sort") != null ? params.get("sort") : 0);
        model.addAttribute("title", category.getC_name());
        model.addAttribute("models", "/product/category/" + slug);
        return "product/list";
    }

    @PostMapping("rating/{id}")
    public ResponseEntity<MessageEntity> rating(@PathVariable("id") Integer id, @RequestParam Map<String, String> params, Model model) throws ClassNotFoundException {
        User user = CommonUtils.getCurrentUser();
        Product product = productService.show(id);
        Rating rating = new Rating();
        rating.setR_user_id(user);
        rating.setProduct(product);
        rating.setR_content(params.get("content"));
        rating.setR_number(Integer.valueOf(params.get("review")));
        rating.setR_status(1);
        ratingRepository.save(rating);
        RatingResponse response = RatingResponse.builder()
                .message("Cập nhật thành công")
                .build();
        return ResponseEntity.ok(new MessageEntity(200, response));
    }
}
