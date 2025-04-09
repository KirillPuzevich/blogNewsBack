package com.software.blognews.controllers;

import com.software.blognews.dto.EntertainmentNewsRequest;
import com.software.blognews.dto.EntertainmentNewsResponse;
import com.software.blognews.dto.NewsListResponse; // Import NewsListResponse
import com.software.blognews.service.EntertainmentNewsService;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entertainment-news")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.100.11:3000"})
public class EntertainmentNewsController {

    private final EntertainmentNewsService entertainmentNewsService;

    public EntertainmentNewsController(EntertainmentNewsService entertainmentNewsService) {
        this.entertainmentNewsService = entertainmentNewsService;
    }

    @PostMapping
    public ResponseEntity<EntertainmentNewsResponse> addEntertainmentNews(@Valid @RequestBody EntertainmentNewsRequest entertainmentNewsRequest) {
        EntertainmentNewsResponse response = entertainmentNewsService.addEntertainmentNews(entertainmentNewsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntertainmentNews(@PathVariable Long id) {
        entertainmentNewsService.deleteEntertainmentNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<NewsListResponse> getEntertainmentNews(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String ordering,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        NewsListResponse newsList = entertainmentNewsService.getEntertainmentNews(search, ordering, limit, offset);
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentNewsResponse> findById(@PathVariable Long id) {
        return entertainmentNewsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}