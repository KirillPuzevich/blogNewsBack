package com.software.blognews.controllers;

import com.software.blognews.dto.TechnologyNewsRequest;
import com.software.blognews.dto.TechnologyNewsResponse;
import com.software.blognews.dto.NewsListResponse; // Import NewsListResponse
import com.software.blognews.service.TechnologyNewsService;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technology-news")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.100.11:3000"})
public class TechnologyNewsController {

    private final TechnologyNewsService technologyNewsService;

    public TechnologyNewsController(TechnologyNewsService technologyNewsService) {
        this.technologyNewsService = technologyNewsService;
    }

    @PostMapping
    public ResponseEntity<TechnologyNewsResponse> addTechnologyNews(@Valid @RequestBody TechnologyNewsRequest technologyNewsRequest) {
        TechnologyNewsResponse response = technologyNewsService.addTechnologyNews(technologyNewsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnologyNews(@PathVariable Long id) {
        technologyNewsService.deleteTechnologyNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<NewsListResponse> getTechnologyNews(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String ordering,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        NewsListResponse newsList = technologyNewsService.getTechnologyNews(search, ordering, limit, offset);
        return ResponseEntity.ok(newsList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TechnologyNewsResponse> findById(@PathVariable Long id) {
        return technologyNewsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}