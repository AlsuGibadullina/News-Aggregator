package com.kpfu.itis.NewsAggregator.models.entities;

import lombok.*;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsTopicId implements Serializable {
    private Long newsId;
    private Long topicId;


}
