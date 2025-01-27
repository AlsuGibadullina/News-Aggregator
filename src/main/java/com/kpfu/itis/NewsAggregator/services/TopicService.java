package com.kpfu.itis.NewsAggregator.services;



import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import com.kpfu.itis.NewsAggregator.models.entities.UserTopic;
import com.kpfu.itis.NewsAggregator.models.entities.UserTopicId;
import com.kpfu.itis.NewsAggregator.repositories.TopicRepository;
import com.kpfu.itis.NewsAggregator.repositories.UserTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserTopicRepository userTopicRepository;

    /**
     * Создание (или получение) темы по имени
     */
    public Topic getOrCreateTopic(String name) {
        Optional<Topic> existing = topicRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get();
        } else {
            Topic newTopic = new Topic();
            newTopic.setName(name);
            return topicRepository.save(newTopic);
        }
    }

    /**
     * Получить все доступные темы в системе
     */
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    /**
     * Привязать тему к пользователю (добавить в user_topics)
     */
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

    /**
     * Удалить тему у пользователя
     */
    public void removeTopicFromUser(Topic topic, User user) {
        UserTopicId id = new UserTopicId(user.getId(), topic.getId());
        if (userTopicRepository.existsById(id)) {
            userTopicRepository.deleteById(id);
        }
    }
}
