package com.kpfu.itis.NewsAggregator.repositories;


import com.kpfu.itis.NewsAggregator.models.entities.News;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopic;
import com.kpfu.itis.NewsAggregator.models.entities.NewsTopicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsTopicRepository extends JpaRepository<NewsTopic, NewsTopicId> {

    @Query("select nt.news from NewsTopic nt where nt.topic.name = :topicName order by nt.news.publishedAt desc")
    Page<News> findNewsByTopicName(@Param("topicName") String topicName, Pageable pageable);
}
