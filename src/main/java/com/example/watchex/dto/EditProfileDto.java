package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileDto {

    @NotEmpty(message = "Thiếu tên khách hàng")
    private String name;

    private String password;
    private MultipartFile avatar = null;

    private String oldpassword;
    private String re_password;

    @NotEmpty(message = "Thiếu số điện thoại")
    private String phone;

    @NotEmpty(message = "Thiếu địa chỉ")
    private String address;

    private String birthday;
    private Integer gender;
}
