package com.software.blognews.service;

import com.software.blognews.dto.TechnologyNewsRequest;
import com.software.blognews.dto.TechnologyNewsResponse;
import com.software.blognews.dto.NewsListResponse;
import com.software.blognews.models.Category;
import com.software.blognews.models.TechnologyNews;
import com.software.blognews.repositories.CategoryRepository;
import com.software.blognews.repositories.TechnologyNewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TechnologyNewsService {

    private final TechnologyNewsRepository technologyNewsRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public TechnologyNewsService(TechnologyNewsRepository technologyNewsRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.technologyNewsRepository = technologyNewsRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public TechnologyNewsResponse addTechnologyNews(TechnologyNewsRequest technologyNewsRequest) {
        TechnologyNews technologyNews = modelMapper.map(technologyNewsRequest, TechnologyNews.class);
        Long categoryId = technologyNewsRequest.getCategoryId();

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            technologyNews.setCategory(category);
        }

        TechnologyNews savedNews = technologyNewsRepository.save(technologyNews);
        return modelMapper.map(savedNews, TechnologyNewsResponse.class);
    }

    public void deleteTechnologyNews(Long id) {
        technologyNewsRepository.deleteById(id);
    }

    public NewsListResponse getTechnologyNews(String search, String ordering, int limit, int offset) {
        List<TechnologyNews> newsList;

        if (search != null && !search.isEmpty()) {
            newsList = technologyNewsRepository.findByTitleContaining(search);
        } else {
            newsList = technologyNewsRepository.findAll();
        }


        switch (ordering) {
            case "id":
                newsList = newsList.stream().sorted(Comparator.comparingLong(TechnologyNews::getId)).collect(Collectors.toList());
                break;
            case "title":
                newsList = newsList.stream().sorted(Comparator.comparing(TechnologyNews::getTitle, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
                break;
            case "publishedAt":
                newsList = newsList.stream().sorted(Comparator.comparing(TechnologyNews::getPublishedAt).reversed()).collect(Collectors.toList());
                break;
            default:
                break;
        }

        int totalSize = newsList.size();
        List<TechnologyNews> paginatedNewsList = newsList.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        List<TechnologyNewsResponse> resultList = paginatedNewsList.stream()
                .map(news -> modelMapper.map(news, TechnologyNewsResponse.class))
                .collect(Collectors.toList());

        return new NewsListResponse(totalSize, resultList);
    }

    public Optional<TechnologyNewsResponse> findById(Long id) {
        Optional<TechnologyNews> technologyNews = technologyNewsRepository.findById(id);
        return technologyNews.map(news -> modelMapper.map(news, TechnologyNewsResponse.class));
    }
}