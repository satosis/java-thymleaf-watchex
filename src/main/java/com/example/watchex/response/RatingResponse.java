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
public class RatingResponse {

    @JsonProperty("message")
    private String message;
}
