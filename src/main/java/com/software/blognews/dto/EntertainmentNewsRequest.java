package com.software.blognews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntertainmentNewsRequest {
    @NotBlank(message = "Строка не должна быть пустой")
    private String title;

    @NotBlank(message = "Строка не должна быть пустой")
    private String imageUrl;

    @NotBlank(message = "Строка не должна быть пустой")
    private String summary;

    private Long categoryId;

}