package com.example.watchex.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto implements Serializable {
    private Integer id;
    private Boolean voided;
    private Integer price;
    private Integer sort;
    private Integer pageIndex=0;
    private Integer pageSize=5;
    private String keyword;
    private String asc;
    private String desc="createdDate";
    public SearchDto(Integer pageSize,Integer pageIndex){
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }
}
