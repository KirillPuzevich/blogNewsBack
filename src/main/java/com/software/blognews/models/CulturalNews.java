package com.software.blognews.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cultural_news")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class CulturalNews {
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
        publishedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
