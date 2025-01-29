package com.kpfu.itis.NewsAggregator.controllers;

import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import com.kpfu.itis.NewsAggregator.services.TopicService;
import com.kpfu.itis.NewsAggregator.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;

    /**
     * GET /api/topics
     * Возвращаем все темы, которые есть в базе
     */
    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    /**
     * POST /api/topics/user/{userId}/add
     * Body: {"topicName": "Sports"} (для примера)
     */
    @PostMapping("/user/{userId}/add")
    public String addTopicToUser(@PathVariable Long userId, @RequestBody TopicRequest request) {
        User user = userService.getUserById(userId);
        // получаем или создаем тему
        Topic topic = topicService.getByName(request.getTopicName()).get();
        // связываем
        topicService.addTopicToUser(topic, user);
        return "Topic " + topic.getName() + " added to user " + user.getEmail();
    }

    /**
     * DELETE /api/topics/user/{userId}/remove
     * Body: {"topicName": "Sports"}
     */
    @DeleteMapping("/user/{userId}/remove")
    public String removeTopicFromUser(@PathVariable Long userId, @RequestBody TopicRequest request) {
        User user = userService.getUserById(userId);
        // Получаем тему по имени (если нет, ничего не делаем)
        Topic topic = topicService.getByName(request.getTopicName()).get();
        topicService.removeTopicFromUser(topic, user);
        return "Topic " + topic.getName() + " removed from user " + user.getEmail();
    }

    // DTO (внутри этого же файла или отдельно)
    static class TopicRequest {
        private String topicName;
        public String getTopicName() {
            return topicName;
        }
        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }
    }
}
