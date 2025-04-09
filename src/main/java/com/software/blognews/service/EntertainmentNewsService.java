package com.software.blognews.service;

import com.software.blognews.dto.EntertainmentNewsRequest;
import com.software.blognews.dto.EntertainmentNewsResponse;
import com.software.blognews.dto.NewsListResponse;
import com.software.blognews.models.Category;
import com.software.blognews.models.EntertainmentNews;
import com.software.blognews.repositories.CategoryRepository;
import com.software.blognews.repositories.EntertainmentNewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntertainmentNewsService {

    private final EntertainmentNewsRepository entertainmentNewsRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public EntertainmentNewsService(EntertainmentNewsRepository entertainmentNewsRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.entertainmentNewsRepository = entertainmentNewsRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public EntertainmentNewsResponse addEntertainmentNews(EntertainmentNewsRequest entertainmentNewsRequest) {
        EntertainmentNews entertainmentNews = modelMapper.map(entertainmentNewsRequest, EntertainmentNews.class);
        Long categoryId = entertainmentNewsRequest.getCategoryId();

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            entertainmentNews.setCategory(category);
        }

        EntertainmentNews savedNews = entertainmentNewsRepository.save(entertainmentNews);
        return modelMapper.map(savedNews, EntertainmentNewsResponse.class);
    }

    public void deleteEntertainmentNews(Long id) {
        entertainmentNewsRepository.deleteById(id);
    }

    public NewsListResponse getEntertainmentNews(String search, String ordering, int limit, int offset) {
        List<EntertainmentNews> newsList;

        // Поиск по заголовку
        if (search != null && !search.isEmpty()) {
            newsList = entertainmentNewsRepository.findByTitleContaining(search);
        } else {
            newsList = entertainmentNewsRepository.findAll(); // Если нет поиска, получаем все
        }

        switch (ordering) {
            case "id":
                newsList = newsList.stream().sorted(Comparator.comparingLong(EntertainmentNews::getId)).collect(Collectors.toList());
                break;
            case "title":
                newsList = newsList.stream().sorted(Comparator.comparing(EntertainmentNews::getTitle, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
                break;
            case "publishedAt":
                newsList = newsList.stream().sorted(Comparator.comparing(EntertainmentNews::getPublishedAt).reversed()).collect(Collectors.toList());
                break;
            default:
                break;
        }

        int totalSize = newsList.size();
        List<EntertainmentNews> paginatedNewsList = newsList.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        List<EntertainmentNewsResponse> resultList = paginatedNewsList.stream()
                .map(news -> modelMapper.map(news, EntertainmentNewsResponse.class))
                .collect(Collectors.toList());

        return new NewsListResponse(totalSize, resultList);
    }

    public Optional<EntertainmentNewsResponse> findById(Long id) {
        Optional<EntertainmentNews> entertainmentNews = entertainmentNewsRepository.findById(id);
        return entertainmentNews.map(news -> modelMapper.map(news, EntertainmentNewsResponse.class));
    }
}