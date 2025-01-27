package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalNewsDto {
    private String title;
    private String content;     // или description, если у Bing другое поле
    private String sourceName;  // bbc-news, cnn, ...
    private LocalDateTime publishedAt;
    private String url;
    // можно добавить сюда другие поля (автор, идентификатор и т.д.)
}
