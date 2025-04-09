package com.software.blognews.controllers;

import com.software.blognews.dto.SportNewsRequest;
import com.software.blognews.dto.SportNewsResponse;
import com.software.blognews.dto.NewsListResponse; // Import NewsListResponse for consistency
import com.software.blognews.service.SportNewsService;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sport-news")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.100.11:3000"})
public class SportNewsController {

    private final SportNewsService sportNewsService;

    public SportNewsController(SportNewsService sportNewsService) {
        this.sportNewsService = sportNewsService;
    }


    @PostMapping
    public ResponseEntity<SportNewsResponse> addSportNews(@Valid @RequestBody SportNewsRequest sportNewsRequest) {
        SportNewsResponse response = sportNewsService.addSportNews(sportNewsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSportNews(@PathVariable Long id) {
        sportNewsService.deleteSportNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<NewsListResponse> getSportNews(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String ordering,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        NewsListResponse response = sportNewsService.getSportNews(search, ordering, limit, offset);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportNewsResponse> findById(@PathVariable Long id) {
        return sportNewsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}