package com.example.watchex.controller.Admin;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Product;
import com.example.watchex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 1;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        model.addAttribute("title", "Quản lý sản phẩm");
        return "admin/products/index";
    }

    private Model findPaginate(int page, Model model) {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", "10");
        params.put("page", String.valueOf(page));
        Page<ProductDto> products = productService.get(params);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("products", products);
        model.addAttribute("models", "product");
        model.addAttribute("title", "Products Management");
        return model;
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Thêm sản phẩm");
        return "admin/products/create";
    }

    @PostMapping("save")
    public String save(Product product, RedirectAttributes ra) {
        productService.save(product);
        ra.addFlashAttribute("message", messageSource.getMessage("create_product_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/product";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Product product = productService.show(id);
            model.addAttribute("title", "Sửa sản phẩm " + product.getPro_name());
            model.addAttribute("product", product);
            return "admin/products /edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/product";
        }
    }

    @PostMapping("update")
    public String update(Product product, RedirectAttributes ra) {
        productService.save(product);
        ra.addFlashAttribute("message", messageSource.getMessage("update_product_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/product";
    }

    @GetMapping("delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            productService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_product_success", new Object[0], LocaleContextHolder.getLocale()));
            productService.delete(id);
            return "redirect:/admin/product";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/product";
        }
    }

    @GetMapping("hot/{id}")
    public String hot(@PathVariable("id") Integer id) throws ClassNotFoundException {
        Product product = productService.show(id);
        if (product.getPro_hot() == 0) {
            product.setPro_hot(1);
        }
        if (product.getPro_hot() == 1) {
            product.setPro_hot(0);
        }
        productService.save(product);
        return "redirect:/admin/product";
    }

    @GetMapping("active/{id}")
    public String active(@PathVariable("id") Integer id) throws ClassNotFoundException {
        Product product = productService.show(id);
        if (product.getPro_active() == 0) {
            product.setPro_active(1);
        }
        if (product.getPro_active() == 1) {
            product.setPro_active(0);
        }
        productService.save(product);
        return "redirect:/admin/product";
    }
}
