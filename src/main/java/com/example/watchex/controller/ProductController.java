package com.example.watchex.controller;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.SearchDto;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.http.HttpHeaders;
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

    @GetMapping("")
    public String index(@RequestParam Map<String, String> params, Model model, RedirectAttributes ra) {
        User user = CommonUtils.getCurrentUser();
        List<Category> categories = categoryService.getAll();
        List<ProductDto> products = productService.getProductsByCategory(1, 100);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Kết quả tìm kiếm cho " + params.get("keyword"));
        return "product/list";
    }
    @GetMapping("/{slug}")
    public String detail(@PathVariable("slug") String slug, Model model, RedirectAttributes ra) {
        User user = CommonUtils.getCurrentUser();
        List<Category> categories = categoryService.getAll();
        ProductDto product = productService.findBySlug(slug);
        List<ProductDto> productSuggest = productService.getProductsByCategory(product.getCategory().getId(), 3);
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
