package com.software.blognews.repositories;

import com.software.blognews.models.SportNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportNewsRepository extends JpaRepository<SportNews, Long> {
    List<SportNews> findByTitleContaining(String title);
    List<SportNews> findAllByOrderByIdAsc();
    List<SportNews> findAllByOrderByTitleAsc();
    List<SportNews> findAllByOrderByPublishedAtDesc();
}