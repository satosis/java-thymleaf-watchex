package com.example.watchex.controller.Admin;

import com.example.watchex.entity.Keyword;
import com.example.watchex.service.KeywordService;
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
public class AdminKeywordController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private KeywordService keywordService;

    @GetMapping("keywords")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 0;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        return "admin/keywords/index";
    }

    private Model findPaginate(int page, Model model){
        Page<Keyword> keywords = keywordService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", keywords.getTotalPages());
        model.addAttribute("totalItems", keywords.getTotalElements());
        model.addAttribute("keywords", keywords);
        model.addAttribute("models", "keywords");
        model.addAttribute("title", "Keywords Management");
        return model;
    }
    @GetMapping("keyword/create")
    public String create(Model model) {
        model.addAttribute("keyword", new Keyword());
        return "admin/keywords/create";
    }

    @PostMapping("keyword/save")
    public String save(Keyword keyword, RedirectAttributes ra) {
        keywordService.save(keyword);
        ra.addFlashAttribute("message", messageSource.getMessage("create_keyword_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/keywords";
    }

    @GetMapping("keyword/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Keyword keyword = keywordService.show(id);
            model.addAttribute("keyword", keyword);
            return "admin/keywords/edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/keywords";
        }
    }

    @PostMapping("keyword/update")
    public String update(Keyword keyword, RedirectAttributes ra) {
        keywordService.save(keyword);
        ra.addFlashAttribute("message", messageSource.getMessage("update_keyword_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/keywords";
    }

    @GetMapping("keyword/delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            keywordService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_keyword_success", new Object[0], LocaleContextHolder.getLocale()));
            keywordService.delete(id);
            return "redirect:/admin/keywords";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/keywords";
        }
    }


}
