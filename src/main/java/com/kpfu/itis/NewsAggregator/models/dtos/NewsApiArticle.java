package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsApiArticle {
    private String title;
    private String description;
    private String url;
    private String publishedAt;
    private NewsApiSource source;
}