package com.example.watchex.controller.Admin;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Keyword;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.ProductImage;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.KeywordService;
import com.example.watchex.service.ProductImageService;
import com.example.watchex.service.ProductService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductService productService;
    @Autowired
    private KeywordService keywordService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductImageService productImageService;

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

    private void findPaginate(int page, Model model) {
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
    }

    @GetMapping("create")
    public String create(Model model, ProductDto productDto) {
        List<Keyword> keywords = keywordService.getAll();
        List<Category> categories = categoryService.getAll();
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Thêm sản phẩm");
        model.addAttribute("productDto", productDto);
        model.addAttribute("keywords", keywords);
        model.addAttribute("categories", categories);
        return "admin/products/create";
    }

    @PostMapping("save")
    public String save(@Valid @ModelAttribute("productDto") ProductDto productDto,
                       BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/products/create";
        }
        Product product = new Product();
        product.setPro_name(productDto.getPro_name());
        product.setPro_amount(productDto.getPro_amount());
        product.setCategory(productDto.getCategory());
        product.setPro_avatar(productDto.getPro_avatar());
        product.setPro_view(0);
        product.setPro_price(productDto.getPro_price());
        product.setPro_sale(productDto.getPro_sale());
        product.setPro_favourite(0);
        product.setPro_hot(0);
        product.setPro_active(1);
        product.setPro_admin_id(CommonUtils.getCurrentAdmin().getId());
        product.setPro_pay(0);
        product.setPro_description(productDto.getPro_description());
        product.setPro_content(productDto.getPro_content());
        product.setPro_review_total(0);
        product.setPro_review_star(0);
        product.set_wysihtml5_mode("");

        productService.save(product);
        ra.addFlashAttribute("message", messageSource.getMessage("create_product_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/product";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra, ProductDto productDto) {
        try {
            List<Keyword> keywords = keywordService.getAll();
            List<Category> categories = categoryService.getAll();
            Product product = productService.show(id);
            List<ProductImage> productImages = productImageService.findByProduct(product);
            productDto.setPro_name(product.getPro_name());
            productDto.setPro_amount(product.getPro_amount());
            productDto.setCategory(product.getCategory());
            productDto.setPro_avatar(product.getPro_avatar());
            productDto.setPro_price(product.getPro_price());
            productDto.setPro_sale(product.getPro_sale());
            productDto.setPro_description(product.getPro_description());
            productDto.setPro_content(product.getPro_content());
            productDto.setPro_review_total(0);
            productDto.setPro_review_star(0);
            productDto.set_wysihtml5_mode("");
            productDto.setId(product.getId());
            model.addAttribute("title", "Sửa sản phẩm " + product.getPro_name());
            model.addAttribute("productDto", productDto);
            model.addAttribute("keywords", keywords);
            model.addAttribute("categories", categories);
            model.addAttribute("productImages", productImages);
            return "admin/products/edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/product";
        }
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("productDto") ProductDto productDto,
                         BindingResult result,
                         RedirectAttributes ra) throws ClassNotFoundException {
        if (result.hasErrors()) {
            return "admin/products/edit";
        }
        Product product = productService.show(id);
        product.setPro_name(productDto.getPro_name());
        product.setPro_amount(productDto.getPro_amount());
        product.setCategory(productDto.getCategory());
        product.setPro_avatar(productDto.getPro_avatar());
        product.setPro_price(productDto.getPro_price());
        product.setPro_sale(productDto.getPro_sale());
        product.setPro_admin_id(CommonUtils.getCurrentAdmin().getId());
        product.setPro_description(productDto.getPro_description());
        product.setPro_content(productDto.getPro_content());

        productService.save(product);
        ra.addFlashAttribute("message", messageSource.getMessage("update_product_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/product";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
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
