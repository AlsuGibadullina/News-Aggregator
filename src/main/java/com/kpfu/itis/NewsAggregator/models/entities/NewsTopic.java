package com.kpfu.itis.NewsAggregator.models.entities;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Entity
@Table(name = "news_topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsTopic {
    @EmbeddedId
    private NewsTopicId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("newsId")
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;
}

