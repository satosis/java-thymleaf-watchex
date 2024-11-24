package com.example.watchex.controller.Admin;

import com.example.watchex.entity.Rating;
import com.example.watchex.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin/rating")
public class AdminRatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 1;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        model.addAttribute("title", "Quản lý đánh giá");
        return "admin/ratings/index";
    }

    private Model findPaginate(int page, Model model) {
        Page<Rating> ratings = ratingService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ratings.getTotalPages());
        model.addAttribute("totalItems", ratings.getTotalElements());
        model.addAttribute("ratings", ratings);
        model.addAttribute("models", "rating");
        model.addAttribute("title", "Quản lý đánh giá");
        return model;
    }

}
