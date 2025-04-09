package com.software.blognews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NewsListResponse {
    int count;
    List<? extends Object> result;
}
