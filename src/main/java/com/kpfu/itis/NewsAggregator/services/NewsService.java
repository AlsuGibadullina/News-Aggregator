package com.kpfu.itis.NewsAggregator.services;

import com.kpfu.itis.NewsAggregator.models.dtos.NewsDto;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsFilterRequest;
import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopic;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import com.kpfu.itis.NewsAggregator.repositories.NewsRepository;
import com.kpfu.itis.NewsAggregator.repositories.NewsTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsTopicRepository newsTopicRepository;
    private final UserService userService;

    /**
     * Получить все новости с учётом базовых критериев (источник, даты и т.д.)
     * Можно расширять и изменять по необходимости
     */

    /**
     * Получить новости по топику с поддержкой пагинации.
     * @param topicName название топика (например, "Sports", "Politics")
     * @param page номер страницы (начиная с 0)
     * @param size количество новостей на странице (например, 15)
     * @return список NewsDto для заданного топика
     */
    public List<NewsDto> getNewsByTopic(String topicName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Получаем новости через запрос в таблицу news_topics
        Page<News> newsPage = newsTopicRepository.findNewsByTopicName(topicName, pageable);
        return newsPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NewsDto> getAllPaged(int page, int size) {
        // Создаем объект Pageable с сортировкой по publishedAt в порядке убывания
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        // Выполняем запрос к БД с использованием пагинации
        Page<News> newsPage = newsRepository.findAll(pageable);
        // Преобразуем сущности в DTO и возвращаем
        return newsPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable("popularRequests")
    public List<NewsDto> getAll() {
        List<News> allNews = newsRepository.findAll();
        return allNews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public News getById(Long id) {
        News news = newsRepository.findById(id).get();
        return news;
    }

    /**
     * Получить "персонализированную" ленту новостей для пользователя
     * То есть, с учётом тем (Topic), которые пользователь выбрал.
     */
    public List<NewsDto> getPersonalizedNews(Long userId) {
        // находим пользователя
        User user = userService.getUserById(userId).get();

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
    public NewsDto convertToDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setId(news.getId());
        dto.setUrl(news.getUrl());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setSource(news.getSource());
        dto.setPublishedAt(news.getPublishedAt());
        dto.setCommentsCount(news.getCommentsCount());

        return dto;
    }
}
