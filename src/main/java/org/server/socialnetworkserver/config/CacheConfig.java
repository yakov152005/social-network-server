/*
package org.server.socialnetworkserver.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "profileCache",
                "homeFeedCache",
                "userDetailsCache",
                "postLikesCache",
                "postCommentsCache",
                "followersCache",
                "followCountCache",
                "numUsersCache",
                "allUsersCache",
                "userPostsCache"
        );
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(1000)
        );
        System.out.println("🟢 Caffeine Cache Manager Loaded Successfully!");
        return cacheManager;
    }
}

 */