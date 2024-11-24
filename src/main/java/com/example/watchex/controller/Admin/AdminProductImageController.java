package com.example.watchex.controller;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.RatingDto;
import com.example.watchex.entity.*;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.repository.RatingRepository;
import com.example.watchex.response.RatingResponse;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductImageService;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.UserFavouriteService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/product_image")
public class AdminProductImageController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductImageService productImageService;

     @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra, @NonNull HttpServletRequest request) {
        try {
            ProductImage productImage = productImageService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_category_success", new Object[0], LocaleContextHolder.getLocale()));
            productImageService.delete(id);
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
        }
         return "redirect:" + request.getHeader("Referer");
    }
}
