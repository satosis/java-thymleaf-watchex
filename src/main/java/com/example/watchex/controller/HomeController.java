package com.example.watchex.controller;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Product;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
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
        List<Product> productsAccessoriess = productService.getProductsAccessoriess();
        List<Product> productsGlass = productService.getProductsGlass();
        List<Product> productsWatch = productService.getProductsWatch();
        List<Product> listProduct1 = productService.getProductsByCategory(1);
        List<Product> listProduct2 = productService.getProductsByCategory(2);
        List<Product> listProduct3 = productService.getProductsByCategory(3);
        List<Product> listProduct4 = productService.getProductsByCategory(4);
        List<Product> listProduct5 = productService.getProductsByCategory(5);
        List<Product> listProduct6 = productService.getProductsByCategory(6);
        List<Product> listProduct7 = productService.getProductsByCategory(7);

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
