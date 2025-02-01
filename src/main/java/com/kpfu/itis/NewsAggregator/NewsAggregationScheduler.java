package com.kpfu.itis.NewsAggregator;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsAggregationScheduler {

    private final NewsAggregationService aggregationService;

    /**
     * Запускает агрегацию новостей каждые 15 минут.
     * Cron-выражение: "0 0/15 * * * ?" означает каждую 15-ю минуту каждого часа.
     */
    //@Scheduled(cron = "0 0/15 * * * ?")
    public void scheduledAggregation() {
        aggregationService.aggregateAllNews();
    }
}
