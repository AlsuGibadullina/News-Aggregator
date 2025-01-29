package com.kpfu.itis.NewsAggregator.services;


import com.kpfu.itis.NewsAggregator.models.dtos.ExternalNewsDto;
import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import com.kpfu.itis.NewsAggregator.models.entities.UserTopic;
import com.kpfu.itis.NewsAggregator.models.entities.UserTopicId;
import com.kpfu.itis.NewsAggregator.repositories.TopicRepository;
import com.kpfu.itis.NewsAggregator.repositories.UserTopicRepository;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserTopicRepository userTopicRepository;

    // Словарь тем и их ключевых слов
    private Map<String, Set<String>> topicKeywords = new HashMap<>();

    @PostConstruct
    public void init() {
        // Инициализируем темы и их ключевые слова
        // В реальном приложении можно загружать эти данные из базы данных или конфигурационных файлов
        topicKeywords.put("Sports", new HashSet<>(Arrays.asList(
                "football", "soccer", "basketball", "tennis", "cricket", "baseball", "fifa", "nba", "olympics", "athlete", "match", "tournament"
        )));

        topicKeywords.put("Politics", new HashSet<>(Arrays.asList(
                "election", "government", "minister", "policy", "senate", "congress", "president", "law", "democracy", "vote", "parliament", "political"
        )));

        topicKeywords.put("Technology", new HashSet<>(Arrays.asList(
                "technology", "tech", "software", "hardware", "AI", "artificial intelligence", "machine learning", "blockchain", "robotics", "gadgets", "internet", "innovation"
        )));

        topicKeywords.put("Health", new HashSet<>(Arrays.asList(
                "health", "medicine", "medical", "virus", "covid", "pandemic", "doctor", "hospital", "disease", "vaccine", "wellness", "nutrition"
        )));

        topicKeywords.put("Business", new HashSet<>(Arrays.asList(
                "business", "economy", "market", "stocks", "finance", "investment", "startup", "entrepreneur", "trade", "industry", "company", "CEO"
        )));

        // Добавьте другие темы по необходимости

        // Сохраняем темы в базе данных (если они ещё не существуют)
        for (String topicName : topicKeywords.keySet()) {
            topicRepository.findByName(topicName).orElseGet(() -> {
                Topic topic = new Topic();
                topic.setName(topicName);
                return topicRepository.save(topic);
            });
        }
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getByName(String name) {
        Optional<Topic> topic = topicRepository.findByName(name);

        return Optional.of(topic.get());
    }

    public void addTopicToUser(Topic topic, User user) {
        UserTopicId id = new UserTopicId(user.getId(), topic.getId());
        if (!userTopicRepository.existsById(id)) {
            UserTopic ut = new UserTopic();
            ut.setId(id);
            ut.setUser(user);
            ut.setTopic(topic);
            userTopicRepository.save(ut);
        }
    }

    public void removeTopicFromUser(Topic topic, User user) {
        UserTopicId id = new UserTopicId(user.getId(), topic.getId());
        if (userTopicRepository.existsById(id)) {
            userTopicRepository.deleteById(id);
        }
    }

    /**
     * Метод для определения тем новости на основе её DTO
     */
    public List<Topic> determineTopics(ExternalNewsDto dto, StanfordCoreNLP pipeline) {
        // Объединяем заголовок и содержание
        String text = dto.getTitle() + " " + dto.getContent();

        // Обработка текста с помощью CoreNLP
        edu.stanford.nlp.pipeline.Annotation document = new edu.stanford.nlp.pipeline.Annotation(text);
        pipeline.annotate(document);

        // Извлекаем леммы из текста
        List<String> lemmas = new ArrayList<>();
        for (edu.stanford.nlp.util.CoreMap sentence : document.get(edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation.class)) {
            for (edu.stanford.nlp.ling.CoreLabel token : sentence.get(edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation.class)) {
                String lemma = token.get(edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation.class).toLowerCase();
                lemmas.add(lemma);
            }
        }

        // Считаем совпадения ключевых слов с темами
        Map<String, Integer> topicMatchCount = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : topicKeywords.entrySet()) {
            String topic = entry.getKey();
            Set<String> keywords = entry.getValue();
            int count = 0;
            for (String lemma : lemmas) {
                if (keywords.contains(lemma)) {
                    count++;
                }
            }
            if (count > 0) {
                topicMatchCount.put(topic, count);
            }
        }

        // Сортируем темы по количеству совпадений
        List<String> sortedTopics = topicMatchCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Возвращаем соответствующие сущности Topic
        List<Topic> matchedTopics = new ArrayList<>();
        for (String topicName : sortedTopics) {
            topicRepository.findByName(topicName).ifPresent(matchedTopics::add);
        }

        return matchedTopics;
    }
}