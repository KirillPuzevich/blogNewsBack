package com.software.blognews.service;

import com.software.blognews.dto.CulturalNewsRequest;
import com.software.blognews.dto.CulturalNewsResponse;
import com.software.blognews.dto.NewsListResponse;
import com.software.blognews.models.Category;
import com.software.blognews.models.CulturalNews;
import com.software.blognews.models.User;
import com.software.blognews.repositories.CategoryRepository;
import com.software.blognews.repositories.CulturalNewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CulturalNewsService {

    private final CulturalNewsRepository culturalNewsRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
     private final UserService userService;

    public CulturalNewsService(CulturalNewsRepository culturalNewsRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, UserService userService) {
        this.culturalNewsRepository = culturalNewsRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    public CulturalNewsResponse addCulturalNews(CulturalNewsRequest culturalNewsRequest) {
        CulturalNews culturalNews = modelMapper.map(culturalNewsRequest, CulturalNews.class);
        Long categoryId = culturalNewsRequest.getCategoryId();

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            culturalNews.setCategory(category);
        }

        CulturalNews savedNews = culturalNewsRepository.save(culturalNews);
        return modelMapper.map(savedNews, CulturalNewsResponse.class);
    }

    public void deleteCulturalNews(Long id) {
        culturalNewsRepository.deleteById(id);
    }

    public NewsListResponse getCulturalNews(String search, String ordering, int limit, int offset) {
        List<CulturalNews> newsList;

        if (search != null && !search.isEmpty()) {
            newsList = culturalNewsRepository.findByTitleContaining(search);
        } else {
            newsList = culturalNewsRepository.findAll();
        }

        switch (ordering) {
            case "id":
                newsList = newsList.stream().sorted(Comparator.comparingLong(CulturalNews::getId)).collect(Collectors.toList());
                break;
            case "title":
                newsList = newsList.stream().sorted(Comparator.comparing(CulturalNews::getTitle, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
                break;
            case "publishedAt":
                newsList = newsList.stream().sorted(Comparator.comparing(CulturalNews::getPublishedAt).reversed()).collect(Collectors.toList());
                break;
            default:
                break;
        }
        int totalSize = newsList.size();
        List<CulturalNews> paginatedNewsList = newsList.stream()
                .skip(offset)
                .limit(limit)
                .toList();

        List<CulturalNewsResponse> resultList = paginatedNewsList.stream()
                .map(news -> modelMapper.map(news, CulturalNewsResponse.class))
                .toList();
        return new NewsListResponse(totalSize, resultList);
    }


    public Optional<CulturalNewsResponse> findById(Long id) {
        User currentUser = userService.getCurrentUser();
        Optional<CulturalNews> culturalNews = culturalNewsRepository.findById(id);
        CulturalNewsResponse culturalNewsResponse = culturalNews.map(news -> modelMapper.map(news, CulturalNewsResponse.class)).get();
        if (culturalNews.get().getUsers().contains(currentUser)) {
            culturalNewsResponse.setLiked(true);
        }
        return Optional.of(culturalNewsResponse);
    }
}