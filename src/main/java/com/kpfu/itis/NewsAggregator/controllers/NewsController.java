package com.kpfu.itis.NewsAggregator.controllers;


import com.kpfu.itis.NewsAggregator.models.dtos.NewsDto;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsFilterRequest;
import com.kpfu.itis.NewsAggregator.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * Пример: POST /api/news/filter
     * В body отправляем JSON с полями: sources, startDate, endDate
     */
    @PostMapping("/filter")
    public List<NewsDto> getNewsByFilter(@RequestBody NewsFilterRequest request) {
        return newsService.getNewsByFilter(request);
    }

    /**
     * Пример: GET /api/news/personalized?userId=123
     * Возвращает персонализированные новости для пользователя
     */
    @GetMapping("/personalized")
    public List<NewsDto> getPersonalizedNews(@RequestParam Long userId) {
        return newsService.getPersonalizedNews(userId);
    }

    @GetMapping("")
    public List<NewsDto> getAll() {
        return newsService.get10LastNews();
    }
}
