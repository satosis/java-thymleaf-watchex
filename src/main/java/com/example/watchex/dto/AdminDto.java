package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Integer id;
    @NotEmpty(message = "Thiếu tên quản trị viên")
    private String name;

    @Email(message = "Email không hợp lệ")
    private String email;
    private String password;

    private String password_confirmation;

}
