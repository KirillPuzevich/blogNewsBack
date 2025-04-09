package com.software.blognews.controllers;

import com.software.blognews.dto.UpdateFavoritesRequest;
import com.software.blognews.dto.UpdateLikedNewsRequest;
import com.software.blognews.dto.UserResponse;
import com.software.blognews.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.100.11:3000"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponse getUserInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/favorites")
    public void updateFavorites(@RequestBody UpdateFavoritesRequest request) {
        userService.updateFavorites(request.getCategoryId());
    }

    @PostMapping("/likedNews")
    public void update(@RequestBody UpdateLikedNewsRequest request) {
        userService.updateLikedNews(request.getCategoryId(), request.getNewsId());
    }
}
