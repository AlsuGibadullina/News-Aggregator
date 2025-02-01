package com.kpfu.itis.NewsAggregator.controllers;


import com.kpfu.itis.NewsAggregator.models.dtos.NewsDto;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsFilterRequest;
import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

//    @PostMapping("/filter")
//    public List<NewsDto> getNewsByFilter(@RequestBody NewsFilterRequest request) {
//        return newsService.getNewsByFilter(request);
//    }

    @GetMapping("/personalized")
    public List<NewsDto> getPersonalizedNews(@RequestParam Long userId) {
        return newsService.getPersonalizedNews(userId);
    }

    @GetMapping("/all")
    public List<NewsDto> getAll() {
        return newsService.getAll();
    }

    /**
     * Эндпоинт для получения новостей по топику с пагинацией.
     * Пример запроса: GET /api/news/by-topic?topic=Sports&page=0&size=15
     */
    @GetMapping("/by-topic")
    public List<NewsDto> getNewsByTopic(
            @RequestParam("topic") String topicName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "15") int size) {
        return newsService.getNewsByTopic(topicName, page, size);
    }

    @GetMapping("/all-paged")
    public List<NewsDto> getAllPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "15") int size
    ) {
        return newsService.getAllPaged(page, size);
    }

    @GetMapping("/{id}")
    public NewsDto getNewsById(@PathVariable("id") Long id) {
        News news = newsService.getById(id);
        return newsService.convertToDto(news);
    }
}
