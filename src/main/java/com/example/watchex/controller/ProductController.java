package com.example.watchex.controller;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.User;
import com.example.watchex.entity.UserFavourite;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.UserFavouriteService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product/")
public class ProductController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserFavouriteService userFavouriteService;

    @GetMapping("{slug}")
    public String index(@PathVariable("slug") String slug, Model model, RedirectAttributes ra) {
        User user = CommonUtils.getCurrentUser();
        List<Category> categories = categoryService.getAll();
        ProductDto product = productService.findBySlug(slug);
        List<Product> productSuggest = productService.getProductsByCategory(product.getCategory().getId());
        UserFavourite userFavourite = userFavouriteService.getByProductId(product);
        String[] tags= product.getKeywordseo().split(",");
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("productSuggest", productSuggest);
        model.addAttribute("userFavourite", userFavourite);
        model.addAttribute("tags", tags);
        return "product/detail";
    }
}
