package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDataResponse {
    private String status;
    private Integer totalResults;
    private List<NewsDataArticle> results;
}
