package com.example.watchex.controller.Admin;

import com.example.watchex.dto.CategoryDto;
import com.example.watchex.entity.Category;
import com.example.watchex.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 1;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        model.addAttribute("title", "Quản lý danh mục");
        return "admin/categories/index";
    }

    private void findPaginate(int page, Model model) {
        Page<Category> categories = categoryService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("totalItems", categories.getTotalElements());
        model.addAttribute("categories", categories);
        model.addAttribute("models", "categories");
        model.addAttribute("title", "Categories Management");
    }

    @GetMapping("create")
    public String create(Model model, CategoryDto categoryDto) {
        model.addAttribute("title", "Thêm danh mục");
        model.addAttribute("categoryDto", categoryDto);
        return "admin/categories/create";
    }

    @PostMapping("save")
    public String save(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                       BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/categories/create";
        }
        Category category = new Category();
        category.setC_name(categoryDto.getC_name());
        category.setC_cate(categoryDto.getC_cate());

        categoryService.save(category);
        ra.addFlashAttribute("message", messageSource.getMessage("create_category_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/category";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra, CategoryDto categoryDto) {
        try {
            Category category = categoryService.show(id);
            categoryDto.setC_name(category.getC_name());
            categoryDto.setC_cate(category.getC_cate());
            categoryDto.setId(category.getId());
            model.addAttribute("title", "Sửa danh mục " + category.getC_name());
            model.addAttribute("categoryDto", categoryDto);
            return "admin/categories/edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/category";
        }
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                         BindingResult result,
                         RedirectAttributes ra) throws ClassNotFoundException {
        if (result.hasErrors()) {
            return "admin/categories/edit";
        }
        Category category = categoryService.show(id);
        category.setC_name(categoryDto.getC_name());
        category.setC_cate(categoryDto.getC_cate());

        categoryService.save(category);
        ra.addFlashAttribute("message", messageSource.getMessage("update_category_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/category";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Category category = categoryService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_category_success", new Object[0], LocaleContextHolder.getLocale()));
            categoryService.delete(id);
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/category";
    }


}
