package com.software.blognews.repositories;

import com.software.blognews.models.EntertainmentNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntertainmentNewsRepository extends JpaRepository<EntertainmentNews, Long>{
    List<EntertainmentNews> findByTitleContaining(String title);

    List<EntertainmentNews> findAllByOrderByIdAsc();

    List<EntertainmentNews> findAllByOrderByTitleAsc();

    List<EntertainmentNews> findAllByOrderByPublishedAtDesc();
}
