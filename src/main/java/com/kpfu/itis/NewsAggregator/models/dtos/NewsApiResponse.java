package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class NewsApiResponse {
    private String status;
    private Integer totalResults;
    private List<NewsApiArticle> articles;
}