package com.kpfu.itis.NewsAggregator.repositories;

import com.kpfu.itis.NewsAggregator.models.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    // тема по названию (например, "sport")
    Optional<Topic> findByName(String name);

}