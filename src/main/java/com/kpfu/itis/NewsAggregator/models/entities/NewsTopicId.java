package com.kpfu.itis.NewsAggregator.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsTopicId implements Serializable {
    private Long newsId;
    private Long topicId;
}
