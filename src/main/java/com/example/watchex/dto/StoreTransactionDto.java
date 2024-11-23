package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreTransactionDto {

    @NotEmpty(message = "Thiếu tên khách hàng")
    private String name;

    @NotEmpty(message = "Thiếu số điện thoại")
    private String phone;

    @NotEmpty(message = "Thiếu địa chỉ")
    private String address;

    @NotEmpty(message = "Thiếu email")
    @Email(message = "Email không hợp lệ")
    private String email;

    private String note;
    private Integer type;

}
