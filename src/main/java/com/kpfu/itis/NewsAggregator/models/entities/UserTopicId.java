package com.kpfu.itis.NewsAggregator.models.entities;

import lombok.*;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserTopicId implements Serializable {
    private Long userId;
    private Long topicId;
}
