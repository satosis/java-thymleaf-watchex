package com.example.watchex.controller.Admin;

import com.example.watchex.dto.KeywordDto;
import com.example.watchex.entity.Keyword;
import com.example.watchex.service.KeywordService;
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
@RequestMapping("/admin/keyword")
public class AdminKeywordController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private KeywordService keywordService;

    @GetMapping("")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 1;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        model.addAttribute("title", "Quản lý danh mục");
        return "admin/keywords/index";
    }

    private void findPaginate(int page, Model model) {
        Page<Keyword> keywords = keywordService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", keywords.getTotalPages());
        model.addAttribute("totalItems", keywords.getTotalElements());
        model.addAttribute("keywords", keywords);
        model.addAttribute("models", "keywords");
        model.addAttribute("title", "keywords Management");
    }

    @GetMapping("create")
    public String create(Model model, KeywordDto keywordDto) {
        model.addAttribute("title", "Thêm danh mục");
        model.addAttribute("keywordDto", keywordDto);
        return "admin/keywords/create";
    }

    @PostMapping("save")
    public String save(@Valid @ModelAttribute("keywordDto") KeywordDto keywordDto,
                       BindingResult result, RedirectAttributes ra,
                       @NonNull HttpServletRequest request) {
        if (result.hasErrors()) {
            return "admin/keywords/create";
        }
        Keyword keyword = new Keyword();
        keyword.setK_name(keywordDto.getK_name());
        keyword.setK_description(keywordDto.getK_description());
        keyword.setK_active(1);
        keyword.setK_hot(1);

        keywordService.save(keyword);
        ra.addFlashAttribute("message", messageSource.getMessage("create_keyword_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/keyword";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra, KeywordDto keywordDto) {
        try {
            Keyword keyword = keywordService.show(id);
            keywordDto.setK_name(keyword.getK_name());
            keywordDto.setK_description(keyword.getK_description());
            keywordDto.setId(keyword.getId());
            model.addAttribute("title", "Sửa danh mục " + keyword.getK_name());
            model.addAttribute("keywordDto", keywordDto);
            return "admin/keywords/edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/keyword";
        }
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("keywordDto") KeywordDto keywordDto,
                         BindingResult result,
                         RedirectAttributes ra, @NonNull HttpServletRequest request) throws ClassNotFoundException {
        if (result.hasErrors()) {
            return "admin/keywords/edit";
        }
        Keyword keyword = keywordService.show(id);
        keyword.setK_name(keywordDto.getK_name());
        keyword.setK_description(keywordDto.getK_description());

        keywordService.save(keyword);
        ra.addFlashAttribute("message", messageSource.getMessage("update_keyword_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/keyword";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Keyword keyword = keywordService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_keyword_success", new Object[0], LocaleContextHolder.getLocale()));
            keywordService.delete(id);
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/keyword";
    }


}
