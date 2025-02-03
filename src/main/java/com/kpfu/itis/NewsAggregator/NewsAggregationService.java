package com.kpfu.itis.NewsAggregator;

import com.kpfu.itis.NewsAggregator.models.dtos.ExternalNewsDto;
import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopic;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopicId;
import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import com.kpfu.itis.NewsAggregator.repositories.NewsRepository;
import com.kpfu.itis.NewsAggregator.repositories.NewsTopicRepository;
import com.kpfu.itis.NewsAggregator.services.TopicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsAggregationService {


    private final NewsRepository newsRepository;
    private final NewsTopicRepository newsTopicRepository;
    private final TopicService topicService;
    private final NewsApiClient newsApiClient;
    private final NewsDataClient newsDataClient;
    //private final YandexClient yandexClient;
    private final StanfordCoreNLP stanfordCoreNLP;


    /**
     * Метод, который агрегирует все новости из разных источников
     * и возвращает список унифицированных DTO.
     */
    @Transactional
    public void aggregateAllNews() {
        List<ExternalNewsDto> combinedNews = new ArrayList<>();

        // Получаем новости из разных источников
        combinedNews.addAll(newsApiClient.getTopHeadlines("us"));
        combinedNews.addAll(newsDataClient.getTopHeadlines("ru", "ru"));


        // Удаляем дубликаты
        List<ExternalNewsDto> uniqueNews = removeDuplicates(combinedNews);

        // Обрабатываем каждую новость
        for (ExternalNewsDto dto : uniqueNews) {
            saveNewsWithTopics(dto);
        }
    }


    /**
     * Сохранение новости и привязка её к темам.
     */

    private void saveNewsWithTopics(ExternalNewsDto dto) {
        // Проверяем, существует ли новость уже (по URL)
        Optional<News> existingNewsOpt = newsRepository.findByUrl(dto.getUrl());
        News news;

        if (existingNewsOpt.isPresent()) {
            // Новость существует, обновляем её поля
            news = existingNewsOpt.get();
            news.setTitle(dto.getTitle());
            news.setContent(dto.getContent());
            news.setPublishedAt(dto.getPublishedAt());
            // Обновляем другие поля при необходимости
        } else {
            // Создаём новую новость без установки ID
            news = new News();
            news.setTitle(dto.getTitle());
            news.setContent(dto.getContent());
            news.setSource(dto.getSourceName());
            news.setPublishedAt(dto.getPublishedAt());
            news.setUrl(dto.getUrl());
            newsRepository.save(news);
        }
        System.out.println(newsRepository.findByUrl(dto.getUrl()));

        // Определяем темы для новости
        List<Topic> topics = topicService.determineTopics(dto, stanfordCoreNLP);

        // Очищаем существующие связи с темами (если обновляем)
//        news.getNewsTopics().clear();

        // Привязываем новые темы
        for (Topic topic : topics) {
            NewsTopic newsTopic = new NewsTopic();

            // Инициализируем NewsTopicId на основе уже сохранённого news.getId() и topic.getId()
            NewsTopicId newsTopicId = new NewsTopicId(news.getId(), topic.getId());
            newsTopic.setId(newsTopicId);

            // Устанавливаем связи
            newsTopic.setNews(news);
            newsTopic.setTopic(topic);

            // Сохраняем NewsTopic
            newsTopicRepository.save(newsTopic);
        }

        // Сохраняем новость снова, чтобы сохранить связи с темами
        newsRepository.save(news);
    }

    public List<ExternalNewsDto> removeDuplicates(List<ExternalNewsDto> articles) {
        // Способ 1: через Set, предварительно переопределив equals/hashCode
        Set<ExternalNewsDto> set = new HashSet<>(articles);
        return new ArrayList<>(set);

        // Способ 2: вручную, сравнивая поля, если не хотите equals/hashCode
    }


}