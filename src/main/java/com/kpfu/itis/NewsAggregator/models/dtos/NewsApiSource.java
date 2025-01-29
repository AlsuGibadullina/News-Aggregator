package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class NewsApiSource {
    private String id;
    private String name;
}