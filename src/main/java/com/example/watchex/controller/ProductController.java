package com.example.watchex.controller;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.RatingDto;
import com.example.watchex.entity.Cart;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.UserFavourite;
import com.example.watchex.repository.RatingRepository;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.UserFavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String detail(@PathVariable("slug") String slug, Model model) {
        List<Category> categories = categoryService.getAll();
        ProductDto product = productService.findBySlug(slug);

        List<RatingDto> ratings = ratingRepository.getRatingProduct(product.getId());
        List<ProductDto> productSuggest = productService.getProductsByCategory(product.getCategory().getId(), 3);
        UserFavourite userFavourite = userFavouriteService.getByProductId(product);
        String[] tags= product.getKeywordseo().split(",");
        model.addAttribute("cartCount", Cart.cart.size());
        model.addAttribute("product", product);
        model.addAttribute("ratings", ratings);
        model.addAttribute("categories", categories);
        model.addAttribute("productSuggest", productSuggest);
        model.addAttribute("userFavourite", userFavourite);
        model.addAttribute("tags", tags);
        return "product/detail";
    }

    @GetMapping("/category/{slug}")
    public String category(@PathVariable("slug") String slug, @RequestParam Map<String, String> params, Model model) {
        List<Category> categories = categoryService.getAll();
        Category category = categoryService.findBySlug(slug);
        Page<ProductDto> products = productService.findBySlugCategory(slug, params);
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
}
