package com.kpfu.itis.NewsAggregator;

import com.kpfu.itis.NewsAggregator.models.dtos.ExternalNewsDto;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsDataArticle;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsDataClient {
    private final WebClient webClient;

    @Value("${newsdata.url}")     // Например, "https://newsdata.io/api/1/news"
    private String dataApiUrl;

    @Value("${newsdata.api-key}") // Ваш API-ключ
    private String dataApiKey;

    /**
     * Метод для получения списка статей (NewsDataArticle) напрямую из NewsData API.
     */
    public List<NewsDataArticle> fetchNewsData(String country, String language) {
        // Пример: https://newsdata.io/api/1/news?apikey=API_KEY&country=ru&language=ru
        NewsDataResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("newsdata.io")
                        .path("/api/1/news")
                        .queryParam("apikey", dataApiKey)
                        .queryParam("country", country)
                        .queryParam("language", language)
                        .build()
                )
                .retrieve()
                .bodyToMono(NewsDataResponse.class)
                .block(); // Блокирующий вызов для упрощения

        if (response != null && response.getResults() != null) {
            return response.getResults();
        }
        return Collections.emptyList();
    }

    /**
     * Метод, аналогичный getTopHeadlines(...) в NewsApiClient,
     * но возвращает уже список ExternalNewsDto.
     */
    public List<ExternalNewsDto> getTopHeadlines(String country, String language) {
        List<NewsDataArticle> articles = fetchNewsData(country, language);

        List<ExternalNewsDto> result = new ArrayList<>();
        for (NewsDataArticle article : articles) {
            ExternalNewsDto dto = mapToExternalNewsDto(article);
            result.add(dto);
        }
        return result;
    }

    /**
     * Преобразование NewsDataArticle → ExternalNewsDto
     */
    private ExternalNewsDto mapToExternalNewsDto(NewsDataArticle article) {
        ExternalNewsDto dto = new ExternalNewsDto();
        dto.setTitle(article.getTitle());
        dto.setContent(article.getDescription());

        // Источник (sourceName)
        dto.setSourceName(article.getSource_name());

        // URL
        dto.setUrl(article.getLink());

        // Преобразуем pubDate (String) в LocalDateTime (если нужно)
        // Если формат даты не ISO и не RFC, то используйте DateTimeFormatter
        // Пример (если там что-то вроде "2025-02-03 03:26:00"):
        try {
            if (article.getPubDate() != null) {
                LocalDateTime ldt = LocalDateTime.parse(article.getPubDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                dto.setPublishedAt(ldt);
            }
        } catch (DateTimeParseException e) {
            // Логирование и оставляем publishedAt = null или обрабатываем иначе
            dto.setPublishedAt(null);
        }

        return dto;
    }
}
