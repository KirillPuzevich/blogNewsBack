package com.software.blognews.controllers;

import com.software.blognews.dto.CulturalNewsRequest;
import com.software.blognews.dto.CulturalNewsResponse;
import com.software.blognews.dto.NewsListResponse;
import com.software.blognews.service.CulturalNewsService;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cultural-news")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.100.11:3000"})
public class CulturalNewsController {

    private final CulturalNewsService culturalNewsService;

    public CulturalNewsController(CulturalNewsService culturalNewsService) {
        this.culturalNewsService = culturalNewsService;
    }

    @PostMapping
    public ResponseEntity<CulturalNewsResponse> addCulturalNews(@Valid @RequestBody CulturalNewsRequest culturalNewsRequest) {
        CulturalNewsResponse response = culturalNewsService.addCulturalNews(culturalNewsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCulturalNews(@PathVariable Long id) {
        culturalNewsService.deleteCulturalNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<NewsListResponse> getCulturalNews(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String ordering,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        NewsListResponse response = culturalNewsService.getCulturalNews(search, ordering, limit, offset);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CulturalNewsResponse> findById(@PathVariable Long id) {
        return culturalNewsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
// @PreAuthorize("hasRole('ADMIN')")