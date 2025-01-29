package com.kpfu.itis.NewsAggregator.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO, представляющее новость для фронтенда/клиента
 */
@Getter
@Setter
@EqualsAndHashCode
public class NewsDto {
    private Long id;
    private String url;
    private String title;
    private String content;
    private String source;
    private LocalDateTime publishedAt;

    // Дополнительно можно возвращать список тем
    private List<String> topics;

    // Возможны и другие поля, например, кол-во комментариев
    private Integer commentsCount;
}

