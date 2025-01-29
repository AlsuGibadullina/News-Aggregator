package com.kpfu.itis.NewsAggregator.services;

import com.kpfu.itis.NewsAggregator.models.dtos.NewsDto;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsFilterRequest;
import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopic;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import com.kpfu.itis.NewsAggregator.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final TopicService topicService; // см. ниже
    private final UserService userService;   // см. ниже

    /**
     * Получить все новости с учётом базовых критериев (источник, даты и т.д.)
     * Можно расширять и изменять по необходимости
     */

    public List<NewsDto> get10LastNews() {
        List<News> allNews = newsRepository.findAll().subList(0, 10);
        System.out.println(allNews);
        return allNews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<NewsDto> getNewsByFilter(NewsFilterRequest filterRequest) {
        // 1) Достаём все новости (неэффективно, если их много).
        //    Лучше делать кастомный метод в репозитории, если критериев много.
        List<News> allNews = newsRepository.findAll();

        // 2) Фильтруем по источникам (если указаны)
        if (filterRequest.getSources() != null && !filterRequest.getSources().isEmpty()) {
            allNews = allNews.stream()
                    .filter(news -> filterRequest.getSources().contains(news.getSource()))
                    .collect(Collectors.toList());
        }

        // 3) Фильтр по датам
        if (filterRequest.getStartDate() != null) {
            LocalDateTime startDateTime = filterRequest.getStartDate().atStartOfDay();
            allNews = allNews.stream()
                    .filter(news -> news.getPublishedAt() != null && news.getPublishedAt().isAfter(startDateTime))
                    .collect(Collectors.toList());
        }
        if (filterRequest.getEndDate() != null) {
            // берем конец дня, если нужно включить всю дату
            LocalDateTime endDateTime = filterRequest.getEndDate().atTime(LocalTime.MAX);
            allNews = allNews.stream()
                    .filter(news -> news.getPublishedAt() != null && news.getPublishedAt().isBefore(endDateTime))
                    .collect(Collectors.toList());
        }

        // 4) Тут можно добавить фильтр по региону, если он у вас где-то хранится.

        // 5) Преобразуем в DTO
        return allNews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить "персонализированную" ленту новостей для пользователя
     * То есть, с учётом тем (Topic), которые пользователь выбрал.
     */
    public List<NewsDto> getPersonalizedNews(Long userId) {
        // находим пользователя
        User user = userService.getUserById(userId);

        // получаем список тем, на которые подписан пользователь
        List<Topic> userTopics = user.getUserTopics().stream()
                .map(ut -> ut.getTopic())
                .collect(Collectors.toList());

        if (userTopics.isEmpty()) {
            // Если пользователь не выбрал никаких тем — вернём все новости
            // Или вернём пустой список — зависит от бизнес-логики
            return newsRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            // Получаем id тем
            Set<Long> topicIds = userTopics.stream().map(Topic::getId).collect(Collectors.toSet());

            // Фильтруем новости, которые связаны с этими темами
            // (для оптимизации можно написать кастомный запрос,
            //  но для наглядности сделаем через stream)
            List<News> allNews = newsRepository.findAll();
//            List<News> filtered = allNews.stream()
//                    .filter(n -> n.getNewsTopics().stream()
//                            .map(NewsTopic::getTopic)
//                            .anyMatch(t -> topicIds.contains(t.getId()))
//                    )
//                    .collect(Collectors.toList());

            return allNews.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Вспомогательный метод конвертации сущности в DTO
     */
    private NewsDto convertToDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setId(news.getId());
        dto.setUrl(news.getUrl());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setSource(news.getSource());
        dto.setPublishedAt(news.getPublishedAt());
        dto.setCommentsCount(news.getCommentsCount());

        // добавляем список названий тем
//        List<String> topicNames = news.getNewsTopics().stream()
//                .map(nt -> nt.getTopic().getName())
//                .collect(Collectors.toList());
//        dto.setTopics(topicNames);

        return dto;
    }
}
