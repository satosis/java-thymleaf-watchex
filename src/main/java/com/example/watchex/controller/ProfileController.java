package com.example.watchex.controller;

import com.example.watchex.dto.EditProfileDto;
import com.example.watchex.dto.JwtResponse;
import com.example.watchex.dto.LoginDto;
import com.example.watchex.dto.RegisterDto;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Token;
import com.example.watchex.entity.User;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.UserService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String edit(Model model, EditProfileDto editProfileDto) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String register(@Valid @ModelAttribute("editProfileDto") EditProfileDto editProfileDto, BindingResult result) throws SQLException, IOException, MessagingException {
        User user = CommonUtils.getCurrentUser();
        if (result.hasErrors()) {
            return "user/edit";
        }
        if (!Objects.equals(editProfileDto.getPassword(), "")) {
            boolean checkSamePassword = editProfileDto.getRe_password().equals(editProfileDto.getPassword());
            if (!checkSamePassword) {
                result.rejectValue("re_password", "error.re_password", "Mật khẩu xác nhận không chính xác !");
                return "user/edit";
            }
            boolean checkPassword = new BCryptPasswordEncoder().matches(editProfileDto.getOldpassword(), user.getPassword());
            if (!checkPassword) {
                result.rejectValue("oldpassword", "error.oldpassword", "Mật khẩu không chính xác !");
                return "user/edit";
            }
            user.setPassword(editProfileDto.getPassword());
        }
        user.setName(editProfileDto.getName());
        user.setPhone(editProfileDto.getPhone());
        user.setGender(editProfileDto.getGender());
        user.setBirthday(editProfileDto.getBirthday());
        user.setAddress(editProfileDto.getAddress());
        userService.save(user);

        return "redirect:/profile";
    }
}
