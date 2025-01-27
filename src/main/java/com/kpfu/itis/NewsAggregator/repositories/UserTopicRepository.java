package com.kpfu.itis.NewsAggregator.repositories;


import com.kpfu.itis.NewsAggregator.models.entities.UserTopic;
import com.kpfu.itis.NewsAggregator.models.entities.UserTopicId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, UserTopicId> {
    // Могут быть кастомные методы, например, найти все UserTopic по userId
}

