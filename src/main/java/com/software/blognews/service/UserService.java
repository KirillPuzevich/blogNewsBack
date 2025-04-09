package com.software.blognews.service;

import com.software.blognews.dto.CategoryResponse;
import com.software.blognews.dto.UserResponse;
import com.software.blognews.models.Category;
import com.software.blognews.models.User;
import com.software.blognews.repositories.CategoryRepository;
import com.software.blognews.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public UserResponse getUserInfo() {
        User currentUser = getCurrentUser();
        UserResponse response = modelMapper.map(currentUser, UserResponse.class);
        List<CategoryResponse> categoryResponseList = currentUser.getFavorites().stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class)).toList();
        response.setFavorites(categoryResponseList);

        return response;
    }

    public void updateFavorites(Long categoryId) {
        User currentUser = getCurrentUser();
        Category newCategory = categoryRepository.findById(categoryId).get();
        List<Category> favorites = currentUser.getFavorites();
        if(favorites.contains(newCategory)) {
            newCategory.getUsers().remove(currentUser);
            currentUser.getFavorites().remove(newCategory);
        } else {
            newCategory.getUsers().add(currentUser);
            currentUser.getFavorites().add(newCategory);
        }

        categoryRepository.save(newCategory);
        repository.save(currentUser);
    }

    public boolean userExists(String username, String email) {
        return repository.existsByUsername(username) || repository.existsByEmail(email);
    }
}
