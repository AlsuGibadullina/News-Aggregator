package com.kpfu.itis.NewsAggregator.configs;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class InfinispanConfig {
    @Bean
    public EmbeddedCacheManager cacheManager() {
        org.infinispan.configuration.cache.Configuration cacheConfig = new org.infinispan.configuration.cache.ConfigurationBuilder()
                .memory().maxCount(1000) // Максимальное количество элементов в кэше
                .expiration().lifespan(60000) // Время жизни записи в кэше (60 секунд)
                .build();

        EmbeddedCacheManager cacheManager = new DefaultCacheManager();
        cacheManager.defineConfiguration("popularRequests", cacheConfig);

        return cacheManager;
    }
}