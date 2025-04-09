package com.software.blognews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLikedNewsRequest {
    private Long categoryId;
    private Long newsId;
}
