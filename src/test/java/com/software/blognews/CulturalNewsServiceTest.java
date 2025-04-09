package com.software.blognews;

import com.software.blognews.dto.CategoryResponse;
import com.software.blognews.dto.CulturalNewsRequest;
import com.software.blognews.dto.CulturalNewsResponse;
import com.software.blognews.models.Category;
import com.software.blognews.models.CulturalNews;
import com.software.blognews.repositories.CategoryRepository;
import com.software.blognews.repositories.CulturalNewsRepository;
import com.software.blognews.service.CulturalNewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CulturalNewsServiceTest {

    @Mock
    private CulturalNewsRepository culturalNewsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CulturalNewsService culturalNewsService;

    private CulturalNewsRequest culturalNewsRequest;
    private CulturalNews culturalNews;
    private CulturalNewsResponse culturalNewsResponse;

    @BeforeEach
    public void setUp() {
        culturalNewsRequest = new CulturalNewsRequest();
        culturalNewsRequest.setTitle("Test Title");
        culturalNewsRequest.setImageUrl("http://example.com/image.jpg");
        culturalNewsRequest.setSummary("Test Summary");
        culturalNewsRequest.setCategoryId(1L);

        culturalNews = new CulturalNews();
        culturalNews.setId(1L);
        culturalNews.setTitle("Test Title");
        culturalNews.setImageUrl("http://example.com/image.jpg");
        culturalNews.setSummary("Test Summary");
        culturalNews.setPublishedAt(LocalDateTime.now());

        culturalNewsResponse = new CulturalNewsResponse();
        culturalNewsResponse.setId(1L);
        culturalNewsResponse.setTitle("Test Title");
        culturalNewsResponse.setImageUrl("http://example.com/image.jpg");
        culturalNewsResponse.setSummary("Test Summary");
        culturalNewsResponse.setPublishedAt(culturalNews.getPublishedAt());
        culturalNewsResponse.setCategory(new CategoryResponse()); // Предполагается, что CategoryResponse определен
    }

    @Test
    public void addCulturalNews_shouldReturnCulturalNewsResponse() {
        when(modelMapper.map(culturalNewsRequest, CulturalNews.class)).thenReturn(culturalNews);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        when(culturalNewsRepository.save(culturalNews)).thenReturn(culturalNews);
        when(modelMapper.map(culturalNews, CulturalNewsResponse.class)).thenReturn(culturalNewsResponse);

        CulturalNewsResponse response = culturalNewsService.addCulturalNews(culturalNewsRequest);

        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("http://example.com/image.jpg", response.getImageUrl());
        assertEquals("Test Summary", response.getSummary());
        verify(culturalNewsRepository, times(1)).save(any(CulturalNews.class));
    }

    @Test
    public void deleteCulturalNews_shouldCallDeleteById() {
        Long newsId = 1L;

        culturalNewsService.deleteCulturalNews(newsId);

        verify(culturalNewsRepository, times(1)).deleteById(newsId);
    }

    @Test
    public void findById_shouldReturnOptionalCulturalNewsResponse() {
        when(culturalNewsRepository.findById(1L)).thenReturn(Optional.of(culturalNews));
        when(modelMapper.map(culturalNews, CulturalNewsResponse.class)).thenReturn(culturalNewsResponse);

        Optional<CulturalNewsResponse> response = culturalNewsService.findById(1L);

        assertTrue(response.isPresent());
        assertEquals("Test Title", response.get().getTitle());
        assertEquals("http://example.com/image.jpg", response.get().getImageUrl());
        assertEquals("Test Summary", response.get().getSummary());
    }
}