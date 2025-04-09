package com.software.blognews.controllers;

import com.software.blognews.dto.JwtAuthenticationResponse;
import com.software.blognews.dto.SignInRequest;
import com.software.blognews.dto.SignUpRequest;
import com.software.blognews.service.AuthenticationService;
import com.software.blognews.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.100.11:3000"})
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/registration")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/check-username-email")
    public ResponseEntity<String> checkUsernameEmail(@RequestParam String username, @RequestParam String email) {
        if (userService.userExists(username, email)) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем или email уже существует");
        }
        return ResponseEntity.ok("Имя пользователя и email доступны");
    }
}

