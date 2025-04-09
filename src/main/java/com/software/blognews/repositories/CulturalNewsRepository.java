package com.software.blognews.repositories;

import com.software.blognews.models.CulturalNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CulturalNewsRepository extends JpaRepository<CulturalNews, Long> {

    List<CulturalNews> findAllByOrderByIdAsc();

    List<CulturalNews> findAllByOrderByTitleAsc();

    List<CulturalNews> findAllByOrderByPublishedAtDesc();

    List<CulturalNews> findByTitleContaining(String title);
}
