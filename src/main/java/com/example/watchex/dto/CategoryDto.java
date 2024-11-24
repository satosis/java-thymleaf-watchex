package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Integer id;

    @NotEmpty(message = "Thiếu tên danh mục")
    private String c_name;

    @NotEmpty(message = "Thiếu danh mục cha")
    private String c_cate;

}
