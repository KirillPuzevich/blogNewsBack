package com.software.blognews.service;

import com.software.blognews.dto.SportNewsRequest;
import com.software.blognews.dto.SportNewsResponse;
import com.software.blognews.dto.NewsListResponse; // Import the NewsListResponse
import com.software.blognews.models.Category;
import com.software.blognews.models.SportNews;
import com.software.blognews.repositories.CategoryRepository;
import com.software.blognews.repositories.SportNewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SportNewsService {

    private final SportNewsRepository sportNewsRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public SportNewsService(SportNewsRepository sportNewsRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.sportNewsRepository = sportNewsRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public SportNewsResponse addSportNews(SportNewsRequest sportNewsRequest) {
        SportNews sportNews = modelMapper.map(sportNewsRequest, SportNews.class);
        Long categoryId = sportNewsRequest.getCategoryId();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            sportNews.setCategory(category);
        }
        SportNews savedNews = sportNewsRepository.save(sportNews);
        return modelMapper.map(savedNews, SportNewsResponse.class);
    }

    public void deleteSportNews(Long id) {
        sportNewsRepository.deleteById(id);
    }

    public NewsListResponse getSportNews(String search, String ordering, int limit, int offset) {
        List<SportNews> newsList;

        // Поиск по заголовку
        if (search != null && !search.isEmpty()) {
            newsList = sportNewsRepository.findByTitleContaining(search);
        } else {
            newsList = sportNewsRepository.findAll();
        }

        switch (ordering) {
            case "id":
                newsList = newsList.stream().sorted(Comparator.comparingLong(SportNews::getId)).collect(Collectors.toList());
                break;
            case "title":
                newsList = newsList.stream().sorted(Comparator.comparing(SportNews::getTitle, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
                break;
            case "publishedAt":
                newsList = newsList.stream().sorted(Comparator.comparing(SportNews::getPublishedAt).reversed()).collect(Collectors.toList());
                break;
            default:
                break;
        }

        int totalSize = newsList.size();
        List<SportNews> paginatedNewsList = newsList.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        List<SportNewsResponse> resultList = paginatedNewsList.stream()
                .map(news -> modelMapper.map(news, SportNewsResponse.class))
                .collect(Collectors.toList());

        return new NewsListResponse(totalSize, resultList);
    }

    public Optional<SportNewsResponse> findById(Long id) {
        Optional<SportNews> sportNews = sportNewsRepository.findById(id);
        return sportNews.map(news -> modelMapper.map(news, SportNewsResponse.class));
    }
}