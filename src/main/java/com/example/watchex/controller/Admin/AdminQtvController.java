package com.example.watchex.controller.Admin;

import com.example.watchex.dto.AdminDto;
import com.example.watchex.entity.Admin;
import com.example.watchex.service.AdminService;
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
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/qtv")
public class AdminQtvController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdminService adminService;

    @GetMapping("")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 1;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        model.addAttribute("title", "Quản lý quản trị viên");
        return "admin/qtvs/index";
    }

    private void findPaginate(int page, Model model) {
        Page<Admin> qtvs = adminService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", qtvs.getTotalPages());
        model.addAttribute("totalItems", qtvs.getTotalElements());
        model.addAttribute("qtvs", qtvs);
        model.addAttribute("models", "qtv");
        model.addAttribute("title", "Quản lý quản trị viên");
    }

    @GetMapping("create")
    public String create(Model model, AdminDto adminDto) {
        model.addAttribute("qtv", new Admin());
        model.addAttribute("title", "Thêm quản trị viên");
        model.addAttribute("adminDto", adminDto);
        return "admin/qtvs/create";
    }

    @PostMapping("save")
    public String save(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                       BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/qtvs/create";
        }
        Admin admin = new Admin();
        if (!Objects.equals(adminDto.getPassword(), "")) {
            if (!adminDto.getPassword().equals(adminDto.getPassword_confirmation())) {
                result.rejectValue("password", "error.password", "Mật khẩu xác nhận không đúng !");
                return "admin/qtvs/create";
            }
            admin.setPassword(adminDto.getPassword());
        } else {
            result.rejectValue("password", "error.password", "Mật khẩu không được để trống !");
            return "admin/qtvs/create";
        }

        if (Objects.equals(adminDto.getEmail(), "")) {
            result.rejectValue("email", "error.email", "Email không được để trống !");
            return "admin/qtvs/create";
        }
        admin.setName(adminDto.getName());
        admin.setEmail(adminDto.getEmail());

        adminService.save(admin);
        ra.addFlashAttribute("message", messageSource.getMessage("create_qtv_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/qtv";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, AdminDto adminDto) throws UserPrincipalNotFoundException {
        Admin admin = adminService.show(id);
        adminDto.setName(admin.getName());
        adminDto.setEmail(admin.getEmail());
        adminDto.setId(admin.getId());
        model.addAttribute("title", "Sửa quản trị viên " + admin.getName());
        model.addAttribute("adminDto", adminDto);
        return "admin/qtvs/edit";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("adminDto") AdminDto adminDto,
                         BindingResult result,
                         RedirectAttributes ra, @NonNull HttpServletRequest request) throws UserPrincipalNotFoundException {
        if (result.hasErrors()) {
            return "admin/qtvs/edit";
        }
        Admin admin = adminService.show(id);
        admin.setName(adminDto.getName());
        if (adminDto.getPassword() != null) {
            if (!adminDto.getPassword().equals(adminDto.getPassword_confirmation())) {
                result.rejectValue("password", "error.password", "Mật khẩu xác nhận không đúng !");
                return "admin/qtvs/edit";
            }
            admin.setPassword(adminDto.getPassword());
        }

        adminService.save(admin);
        ra.addFlashAttribute("message", messageSource.getMessage("update_qtv_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/qtv";
    }

    @GetMapping("delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra, @NonNull HttpServletRequest request) throws UserPrincipalNotFoundException {
        Admin qtv = adminService.show(id);
        ra.addFlashAttribute("message", messageSource.getMessage("delete_qtv_success", new Object[0], LocaleContextHolder.getLocale()));
        adminService.delete(id);
        return "redirect:" + request.getHeader("Referer");
    }

}
