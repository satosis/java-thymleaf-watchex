package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordDto {
    private Integer id;

    @NotEmpty(message = "Thiếu tên từ khóa")
    private String k_name;

    @NotEmpty(message = "Thiếu mô tả")
    private String k_description;

}
