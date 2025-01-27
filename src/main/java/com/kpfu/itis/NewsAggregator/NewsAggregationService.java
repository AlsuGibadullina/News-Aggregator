package com.kpfu.itis.NewsAggregator;

import com.kpfu.itis.NewsAggregator.models.dtos.ExternalNewsDto;
import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsAggregationService {

    private final NewsApiClient newsApiClient;
    //private final BingClient bingClient;
    //private final YandexClient yandexClient;

    /**
     * Метод, который агрегирует все новости из разных источников
     * и возвращает список унифицированных DTO.
     */
    public List<ExternalNewsDto> aggregateAllNews() {
        List<ExternalNewsDto> combinedNews = new ArrayList<>();

        // Допустим, мы собираем топ-заголовки из разных источников
        combinedNews.addAll(newsApiClient.getTopHeadlines("us"));
        //combinedNews.addAll(bingClient.getTopHeadlines("us"));
        //combinedNews.addAll(yandexClient.getTopHeadlines("us"));

        // Здесь можно добавить логику обработки/фильтрации/удаления дублей и т.д.

        return combinedNews;
    }

    public List<ExternalNewsDto> removeDuplicates(List<ExternalNewsDto> articles) {
        // Способ 1: через Set, предварительно переопределив equals/hashCode
        Set<ExternalNewsDto> set = new HashSet<>(articles);
        return new ArrayList<>(set);

        // Способ 2: вручную, сравнивая поля, если не хотите equals/hashCode
    }

    public List<Topic> determineTopics(ExternalNewsDto dto) {
        List<Topic> matchedTopics = new ArrayList<>();
        String text = (dto.getTitle() + " " + dto.getContent()).toLowerCase();

        // Если у вас в БД есть таблица topics, вы можете пройтись по всем темам
        // и проверять их словари (или хотя бы имя)
        // Для упрощения тут показываю «ручной» пример.

        if (text.contains("football") || text.contains("soccer")) {
            // получить из БД topic "sport"
            // matchedTopics.add(topicSport);
        }
        if (text.contains("minister") || text.contains("election")) {
            // matchedTopics.add(topicPolitics);
        }
        if (text.contains("apple") || text.contains("google") || text.contains("microsoft")) {
            // matchedTopics.add(topicTechnology);
        }

        return matchedTopics;
    }



}
