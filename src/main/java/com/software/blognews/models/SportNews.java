package com.software.blognews.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sport_news")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class SportNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    //@Column(name = "summary", columnDefinition = "")
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
}
