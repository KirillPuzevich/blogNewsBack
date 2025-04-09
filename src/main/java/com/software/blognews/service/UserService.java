package com.software.blognews.service;

import com.software.blognews.dto.CategoryResponse;
import com.software.blognews.dto.UserResponse;
import com.software.blognews.models.*;
import com.software.blognews.repositories.*;
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
    private final SportNewsRepository sportNewsRepository;
    private final CulturalNewsRepository culturalNewsRepository;
    private final TechnologyNewsRepository technologyNewsRepository;
    private final EntertainmentNewsRepository entertainmentNewsRepository;
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

    public void updateLikedNews(Long categoryId, Long newsId) {
        User currentUser = getCurrentUser();
        switch (categoryId.intValue()) {
            case 1 -> {
                SportNews sportNews = sportNewsRepository.findById(newsId).get();
                List<SportNews> likedSportNews = currentUser.getLikedSportNews();
                if (likedSportNews.contains(sportNews)) {
                    currentUser.getLikedSportNews().remove(sportNews);
                    sportNews.getUsers().remove(currentUser);
                } else {
                    currentUser.getLikedSportNews().add(sportNews);
                    sportNews.getUsers().add(currentUser);
                }
                sportNewsRepository.save(sportNews);
            }
            case 2 -> {
                CulturalNews culturalNews = culturalNewsRepository.findById(newsId).get();
                List<CulturalNews> likedCulturalNews = currentUser.getLikedCulturalNews();
                if (likedCulturalNews.contains(culturalNews)) {
                    currentUser.getLikedSportNews().remove(culturalNews);
                    culturalNews.getUsers().remove(currentUser);
                } else {
                    currentUser.getLikedCulturalNews().add(culturalNews);
                    culturalNews.getUsers().add(currentUser);
                }
                culturalNewsRepository.save(culturalNews);
            }
            case 3 -> {
                TechnologyNews technologyNews = technologyNewsRepository.findById(newsId).get();
                List<TechnologyNews> likedTechnologyNews = currentUser.getLikedTechnologyNews();
                if (likedTechnologyNews.contains(technologyNews)) {
                    currentUser.getLikedTechnologyNews().remove(technologyNews);
                    technologyNews.getUsers().remove(currentUser);
                } else {
                    currentUser.getLikedTechnologyNews().add(technologyNews);
                    technologyNews.getUsers().add(currentUser);
                }
                technologyNewsRepository.save(technologyNews);
            }
            case 4 -> {
                EntertainmentNews entertainmentNews = entertainmentNewsRepository.findById(newsId).get();
                List<EntertainmentNews> likedEntertainmentNews = currentUser.getLikedEntertainmentNews();
                if (likedEntertainmentNews.contains(entertainmentNews)) {
                    currentUser.getLikedSportNews().remove(entertainmentNews);
                    entertainmentNews.getUsers().remove(currentUser);
                } else {
                    currentUser.getLikedEntertainmentNews().add(entertainmentNews);
                    entertainmentNews.getUsers().add(currentUser);
                }
                entertainmentNewsRepository.save(entertainmentNews);
            }
        }
        repository.save(currentUser);
    }

    public boolean userExists(String username, String email) {
        return repository.existsByUsername(username) || repository.existsByEmail(email);
    }
}
