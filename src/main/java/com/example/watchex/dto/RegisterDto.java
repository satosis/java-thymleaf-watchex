package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotEmpty(message = "Thiếu tên khách hàng")
    private String name;

    @NotEmpty(message = "Thiếu email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Thiếu mật khẩu")
    private String password;

    @NotEmpty(message = "Thiếu mật khẩu xác nhận")
    private String password_confirm;

    @NotEmpty(message = "Thiếu số điện thoại")
    private String phone;
}
