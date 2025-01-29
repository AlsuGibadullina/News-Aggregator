package com.kpfu.itis.NewsAggregator.repositories;

import com.kpfu.itis.NewsAggregator.models.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    // Можно добавить свои кастомные методы. Например, фильтр по дате:
    List<News> findByPublishedAtBetween(LocalDateTime start, LocalDateTime end);

    // Или поиск по source
    List<News> findBySource(String source);

    Optional<News> findByUrl(String url);

    // и т.д.
}