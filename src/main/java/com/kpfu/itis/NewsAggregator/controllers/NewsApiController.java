package com.kpfu.itis.NewsAggregator.controllers;

import com.kpfu.itis.NewsAggregator.models.dtos.NewsDto;
import com.kpfu.itis.NewsAggregator.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsApiController {

    private final NewsService newsService;
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
}
