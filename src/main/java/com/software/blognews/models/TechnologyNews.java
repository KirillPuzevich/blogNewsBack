package com.software.blognews.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "technology_news")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TechnologyNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    private String summary;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    @PrePersist
    private void init() {
        publishedAt = LocalDateTime.now(); // Установка текущей даты
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToMany(mappedBy = "likedTechnologyNews")
    private List<User> users;
}