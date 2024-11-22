package com.example.watchex.response;

import com.example.watchex.entity.Admin;
import com.example.watchex.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartResponse {

    @JsonProperty("message")
    private String message;
    @JsonProperty("totalMoney")
    private String totalMoney;
    @JsonProperty("totalItem")
    private String totalItem;
    @JsonProperty("number")
    private Integer number;
}