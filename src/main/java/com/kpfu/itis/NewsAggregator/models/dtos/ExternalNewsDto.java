package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.*;

import java.time.LocalDateTime;
//Для унификации между всеми апишками
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ExternalNewsDto {
    private String title;
    private String content;
    private String sourceName;
    private LocalDateTime publishedAt;
    private String url;
    private Long id;
}
