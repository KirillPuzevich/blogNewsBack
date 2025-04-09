package com.software.blognews.repositories;

import com.software.blognews.models.TechnologyNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyNewsRepository extends JpaRepository<TechnologyNews, Long> {
    List<TechnologyNews> findAllByOrderByIdAsc();

    List<TechnologyNews> findAllByOrderByTitleAsc();

    List<TechnologyNews> findAllByOrderByPublishedAtDesc();

    List<TechnologyNews> findByTitleContaining(String title);
}
