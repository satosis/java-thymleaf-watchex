package com.example.watchex.controller.Admin;

import com.example.watchex.common.TokenType;
import com.example.watchex.response.AuthenticationResponse;
import com.example.watchex.dto.JwtResponse;
import com.example.watchex.dto.LoginDto;
import com.example.watchex.dto.RegisterDto;
import com.example.watchex.entity.Admin;
import com.example.watchex.entity.Token;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.service.AdminJwtService;
import com.example.watchex.service.AdminService;
import com.example.watchex.service.TokenService;
import com.example.watchex.utils.AdminJwtUtils;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin-auth/")
public class AdminAuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TokenService tokenService;
    private final AdminJwtUtils jwtUtil;

    private final AdminJwtService jwtService;

    public AdminAuthController(AdminJwtUtils jwtUtil, AdminJwtService jwtService) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
    }

    @GetMapping("login")
    public String loginForm(Model model, LoginDto loginDto) {
        loginDto.setEmail("admin@gmail.com");
        loginDto.setPassword("123456789");
        model.addAttribute("loginDto", loginDto);
        return "admin/auth/login";
    }

    @PostMapping("checklogin")
    public String login(@Valid @ModelAttribute("loginDto") LoginDto loginDto,
                        BindingResult result
    ) throws MethodArgumentNotValidException, UsernameNotFoundException {
        if (result.hasErrors()) {
            return "admin/auth/login";
        }
        AuthenticationResponse jwt;
        Admin admin = adminService.findByEmail(loginDto.getEmail());
        if (admin == null) {
            result.rejectValue("email", "error.email", "Tài khoản không tồn tại !");
            return "admin/auth/login";
        }
        boolean checkPassword = new BCryptPasswordEncoder().matches(loginDto.getPassword(), admin.getPassword());
        if (!checkPassword) {
            result.rejectValue("email", "error.email", "Tài khoản hoặc mật khẩu không chính xác !");
            return "admin/auth/login";
        }
        String jwtToken = jwtService.generateToken(admin);
        String refreshToken = jwtService.generateRefreshToken(admin);
        revokeAllUserTokens(admin);
        saveAdminToken(admin, jwtToken);

        jwt = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .admin(admin)
                .build();


        CommonUtils.setCookie("AdminAuthorization", "Bearer " + jwtToken);
        return "redirect:/admin";
//        return ResponseEntity.ok(new MessageEntity(200, jwt));
    }

    private void saveAdminToken(Admin admin, String jwtToken) {
        var token = Token.builder()
                .admin(admin)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
    }

    private void revokeAllUserTokens(Admin admin) {
        var validUserTokens = tokenService.findAllValidTokenByUser(admin.getId());
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
        return "admin/auth/register";
    }

    @PostMapping("auth/registered")
    public ResponseEntity<?> register(@Valid RegisterDto registerDto, @RequestParam("image") MultipartFile file) throws Exception {
        if (adminService.existsByEmail(registerDto.getEmail())) {
            throw new Exception("Email đã được đăng ký !");
        }
        Admin admin = new Admin();
        admin.setName(registerDto.getName());
        admin.setEmail(registerDto.getEmail());
        admin.setPassword(registerDto.getPassword());
        adminService.save(admin);

        Token token = new Token();
        token.setToken(jwtUtil.generateToken(admin));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setAdmin(admin);
        tokenService.createToken(token);
        JwtResponse jwt = new JwtResponse(token.getToken(), admin.getEmail());
        String subject = "Đăng ký thành công";
        String template = "user-register-template";
//        emailService.sendEmail(registerDto.getEmail(), subject, template);
        return ResponseEntity.ok().body(new MessageEntity(200, jwt));

    }

    @GetMapping("/auth/logout")
    public String Logout(){
        CommonUtils.setCookie("AdminAuthorization", "");
        return "redirect:/";
    }
}
