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
        // Инициализируем темы и их ключевые слова.
        // В реальном приложении можно загружать эти данные из БД или конфигурационных файлов.
        topicKeywords.put("Sports", new HashSet<>(Arrays.asList(
                "ESPN", "football", "soccer", "basketball", "tennis", "cricket", "baseball", "fifa", "nba", "olympics", "athlete", "match", "tournament",
                "футбол", "баскетбол", "теннис", "спорт", "соревнования", "чемпионат", "олимпиада"
        )));

        topicKeywords.put("Politics", new HashSet<>(Arrays.asList(
                "democrats", "election", "government", "minister", "policy", "senate", "congress", "president", "law", "democracy", "vote", "parliament", "political",
                "Украина", "Зеленский", "Путин", "президент", "парламент", "выборы", "фракция", "оппозиция", "политика", "власть"
        )));

        topicKeywords.put("Technology", new HashSet<>(Arrays.asList(
                // Английская версия
                "technology", "tech", "software", "hardware", "AI", "artificial intelligence", "machine learning", "blockchain", "robotics", "gadgets", "internet", "innovation",
                // Русская версия
                "технологии", "программное обеспечение", "аппаратное обеспечение", "искусственный интеллект", "машинное обучение", "блокчейн", "робототехника", "гаджеты", "инновация"
        )));

        topicKeywords.put("Health", new HashSet<>(Arrays.asList(
                // Английская версия
                "health", "medicine", "medical", "virus", "covid", "pandemic", "doctor", "hospital", "disease", "vaccine", "wellness", "nutrition",
                // Русская версия
                "здоровье", "медицина", "врач", "больница", "болезнь", "вакцина", "пандемия", "лечебное питание", "фитнес"
        )));

        topicKeywords.put("Business", new HashSet<>(Arrays.asList(
                // Английская версия
                "vp", "earnings", "business", "economy", "market", "stocks", "finance", "investment", "startup", "entrepreneur", "trade", "industry", "company", "CEO",
                // Русская версия
                "бизнес", "экономика", "рынок", "акции", "финансы", "инвестиции", "стартап", "предприниматель", "торговля", "индустрия", "компания", "генеральный директор"
        )));

        // Добавляем ещё 5 тем

        // 1. Entertainment (Развлечения)
        topicKeywords.put("Entertainment", new HashSet<>(Arrays.asList(
                // Английская версия
                "movie", "music", "celebrity", "hollywood", "award", "box office", "streaming", "series", "tv", "concert", "festival", "pop culture",
                // Русская версия
                "кино", "музыка", "звезда", "голливуд", "премия", "кассовый сбор", "стриминг", "сериал", "телевидение", "концерт", "фестиваль", "поп-культура"
        )));

        // 2. Science (Наука)
        topicKeywords.put("Science", new HashSet<>(Arrays.asList(
                // Английская версия
                "science", "research", "space", "nasa", "discovery", "physics", "biology", "chemistry", "experiment", "quantum", "innovation", "laboratory",
                // Русская версия
                "наука", "исследование", "космос", "наса", "открытие", "физика", "биология", "химия", "эксперимент", "квант", "инновация", "лаборатория"
        )));

        // 3. Education (Образование)
        topicKeywords.put("Education", new HashSet<>(Arrays.asList(
                // Английская версия
                "education", "school", "university", "college", "student", "teacher", "curriculum", "scholarship", "learning", "exam", "online education",
                // Русская версия
                "образование", "школа", "университет", "колледж", "студент", "преподаватель", "учебная программа", "стипендия", "обучение", "экзамен", "онлайн образование"
        )));

        // 4. Travel (Путешествия)
        topicKeywords.put("Travel", new HashSet<>(Arrays.asList(
                // Английская версия
                "travel", "tourism", "vacation", "flight", "hotel", "destination", "adventure", "itinerary", "guide", "cruise", "trip", "backpacking", "sightseeing",
                // Русская версия
                "путешествия", "туризм", "отпуск", "рейс", "отель", "направление", "приключение", "маршрут", "гид", "круиз", "поездка", "путешествие с рюкзаком", "достопримечательности"
        )));

        // 5. Culture (Культура)
        topicKeywords.put("Culture", new HashSet<>(Arrays.asList(
                // Английская версия
                "culture", "art", "literature", "museum", "exhibition", "festival", "history", "heritage", "design", "creativity", "tradition", "performance",
                // Русская версия
                "культура", "искусство", "литература", "музей", "выставка", "фестиваль", "история", "наследие", "дизайн", "творчество", "традиция", "выступление"
        )));

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