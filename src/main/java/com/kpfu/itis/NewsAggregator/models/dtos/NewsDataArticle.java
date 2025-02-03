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
public class NewsDataArticle {
    private String article_id;
    private String title;
    private String link;
    private List<String> keywords;
    private List<String> creator;
    private String video_url;
    private String description;
    private String content;
    private String pubDate;
    private String pubDateTZ;
    private String image_url;
    private String source_id;
    private Integer source_priority;
    private String source_name;
    private String source_url;
    private String source_icon;
    private String language;
    private List<String> country;
    private List<String> category;
    private String ai_tag;
    private String sentiment;
    private String sentiment_stats;
    private String ai_region;
    private String ai_org;
    private boolean duplicate;
}
