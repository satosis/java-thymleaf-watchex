package com.example.watchex.controller;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Cart;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.User;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @GetMapping("")
    public String index(Model model) {
        List<Category> categories = categoryService.getAll();
        List<Product> products = productService.getAll();
        List<ProductDto> productsAccessoriess = productService.getProductsAccessoriess();
        List<ProductDto> productsGlass = productService.getProductsGlass();
        List<ProductDto> productsWatch = productService.getProductsWatch();
        List<ProductDto> listProduct1 = productService.getProductsByCategory(1, 15);
        List<ProductDto> listProduct2 = productService.getProductsByCategory(2, 15);
        List<ProductDto> listProduct3 = productService.getProductsByCategory(3, 15);
        List<ProductDto> listProduct4 = productService.getProductsByCategory(4, 15);
        List<ProductDto> listProduct5 = productService.getProductsByCategory(5, 15);
        List<ProductDto> listProduct6 = productService.getProductsByCategory(6, 15);
        List<ProductDto> listProduct7 = productService.getProductsByCategory(7, 15);

        model.addAttribute("cartCount", Cart.cart.size());
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("productsAccessoriess", productsAccessoriess);
        model.addAttribute("productsGlass", productsGlass);
        model.addAttribute("productsWatch", productsWatch);
        model.addAttribute("listProduct1", listProduct1);
        model.addAttribute("listProduct2", listProduct2);
        model.addAttribute("listProduct3", listProduct3);
        model.addAttribute("listProduct4", listProduct4);
        model.addAttribute("listProduct5", listProduct5);
        model.addAttribute("listProduct6", listProduct6);
        model.addAttribute("listProduct7", listProduct7);

        return "index";
    }
}
