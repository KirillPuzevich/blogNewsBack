package com.software.blognews.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String username;
    private String email;
    private Long id;
    private String role;
    private List<CategoryResponse> favorites;
}
