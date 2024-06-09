package com.example.watchex.controller.Admin;

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

import java.util.Map;

@Controller
@RequestMapping("/admin/")
public class AdminProductController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductService productService;

    @GetMapping("products")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 0;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        return "admin/products/index";
    }

    private Model findPaginate(int page, Model model){
        Page<Product> products = productService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("products", products);
        model.addAttribute("models", "products");
        model.addAttribute("title", "Products Management");
        return model;
    }

    @GetMapping("product/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/create";
    }

    @PostMapping("product/save")
    public String save(Product product, RedirectAttributes ra) {
        productService.save(product);
        ra.addFlashAttribute("message", messageSource.getMessage("create_product_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/products";
    }

    @GetMapping("product/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Product product = productService.show(id);
            model.addAttribute("product", product);
            return "admin/products/edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/products";
        }
    }

    @PostMapping("product/update")
    public String update(Product product, RedirectAttributes ra) {
        productService.save(product);
        ra.addFlashAttribute("message", messageSource.getMessage("update_product_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/products";
    }

    @GetMapping("product/delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Product product = productService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_product_success", new Object[0], LocaleContextHolder.getLocale()));
            productService.delete(id);
            return "redirect:/admin/products";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/products";
        }
    }


}
