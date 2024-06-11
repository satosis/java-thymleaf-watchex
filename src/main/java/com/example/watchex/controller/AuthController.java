package com.example.watchex.controller;

import com.example.watchex.common.TokenType;
import com.example.watchex.dto.JwtResponse;
import com.example.watchex.dto.LoginDto;
import com.example.watchex.dto.RegisterDto;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Token;
import com.example.watchex.entity.User;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.service.*;
import com.example.watchex.utils.CommonUtils;
import com.example.watchex.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Blob;
import java.util.List;

@Controller
@RequestMapping("/user/")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TokenService tokenService;
    private final JwtUtils jwtUtil;

    private final JwtService jwtService;

    @Autowired
    private EmailSenderService emailService;

    @ModelAttribute("loginDto")
    public LoginDto loginDto() {
        return new LoginDto();
    }

    public AuthController(JwtUtils jwtUtil, JwtService jwtService) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
    }

    @GetMapping("auth/login")
    public String loginForm(Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories",categories);

        return "auth/login";
    }

    @PostMapping("auth/login")
    public String login(@Valid @ModelAttribute("loginDto") LoginDto loginDto,
                        BindingResult result)
            throws MethodArgumentNotValidException, UsernameNotFoundException {
        AuthenticationResponse jwt;
        User user = userService.findByEmail(loginDto.getEmail());
        if (user == null) {
            result.rejectValue("email", "error.email", "Tài khoản không tồn tại !");
            return "auth/login";
        }
        boolean checkPassword = new BCryptPasswordEncoder().matches(loginDto.getPassword(), user.getPassword());
        if (!checkPassword) {
            result.rejectValue("email", "error.email", "Tài khoản hoặc mật khẩu không chính xác !");
            return "auth/login";
        }
        if (result.hasErrors()) {
            return "auth/login";
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        jwt = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();


        return "redirect:/home";
//        return ResponseEntity.ok(new MessageEntity(200, jwt));
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }

    @GetMapping("auth/register")
    public String registerForm(Model model, RegisterDto registerDto) {
        model.addAttribute("registerDto", registerDto);
        return "auth/register";
    }

    @PostMapping("auth/registered")
    public ResponseEntity<?> register(@Valid RegisterDto registerDto, @RequestParam("image") MultipartFile file) throws Exception {
        boolean checkSamePassword = registerDto.getPassword_confirm().equals(registerDto.getPassword());
        if (!checkSamePassword) {
            return ResponseEntity.ok().body(new MessageEntity(400, "Mật khẩu xác nhận không chính xác !"));
        }
        if (userService.existsByEmail(registerDto.getEmail())) {
            throw new Exception("Email đã được đăng ký !");
        }
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        User user = new User();
        user.setName(registerDto.getName());
        user.setAvatar(blob);
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setPassword(registerDto.getPassword());
        userService.save(user);

        Token token = new Token();
        token.setToken(jwtUtil.generateToken(user));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setUser(user);
        tokenService.createToken(token);
        JwtResponse jwt = new JwtResponse(token.getToken(), user.getEmail());
        String subject = "Đăng ký thành công";
        String template = "user-register-template";
//        emailService.sendEmail(registerDto.getEmail(), subject, template);
        return ResponseEntity.ok().body(new MessageEntity(200, jwt));

    }

    @GetMapping("/auth/logout")
    public String Logout(WebRequest request, SessionStatus status){
        status.setComplete();// đã hoàn thành
        request.removeAttribute("userdto",WebRequest.SCOPE_SESSION);
        return "redirect:/";
    }
}