package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class NewsApiResponse {
    private String status;
    private Integer totalResults;
    private List<NewsApiArticle> articles;
}