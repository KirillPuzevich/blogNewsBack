package com.software.blognews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportNewsResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private String summary;
    private LocalDateTime publishedAt;
    private CategoryResponse category;
    private boolean liked;
}