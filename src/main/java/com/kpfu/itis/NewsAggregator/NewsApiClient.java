package com.kpfu.itis.NewsAggregator;

import com.kpfu.itis.NewsAggregator.models.dtos.ExternalNewsDto;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsApiArticle;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsApiResponse;
import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopic;
import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NewsApiClient {

    private final WebClient webClient; // или RestTemplate, но рекомендуется WebClient

    @Value("${newsapi.url}")
    private String apiUrl; // например, https://newsapi.org/v2
    @Value("${newsapi.api-key}")
    private String apiKey; // ваш ключ

    public List<NewsApiArticle> fetchTopHeadlines(String country) {
        // Пример получения топ-заголовков
        NewsApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("newsapi.org")
                        .path("/v2/top-headlines")
                        .queryParam("country", country)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(NewsApiResponse.class) // вернуть Mono<NewsApiResponse>
                .block(); // блокирующий вызов (упростим пока что)

        if (response != null && response.getArticles() != null) {
            return response.getArticles();
        }
        return Collections.emptyList();
    }

    public List<ExternalNewsDto> getTopHeadlines(String country) {
        List<NewsApiArticle> articles = fetchTopHeadlines(country);
        return articles.stream()
                .map(this::mapToExternalNewsDto)
                .collect(Collectors.toList());
    }


    private ExternalNewsDto mapToExternalNewsDto(NewsApiArticle article) {
        LocalDateTime publishedDateTime = null;
        try {
            // Пример конвертации строки в LocalDateTime, учитывая ISO формат
            publishedDateTime = LocalDateTime.parse(article.getPublishedAt(), DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            // логируем ошибку, или устанавливаем publishedDateTime = null
        }

        return new ExternalNewsDto(
                article.getTitle(),
                article.getDescription(),
                article.getSource() != null ? article.getSource().getName() : "Unknown Source",
                publishedDateTime,
                article.getUrl(),
                article.getId()
        );
    }

}
