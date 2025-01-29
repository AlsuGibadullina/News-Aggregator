package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class NewsApiArticle {
    private String title;
    private String description;
    private String url;
    private Long id;
    private String publishedAt;
    private NewsApiSource source;
}