package dev.scraper.suggestions.infra;

import dev.scraper.suggestions.domain.Link;
import dev.scraper.suggestions.domain.SuggestionsCache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RedisRepository implements SuggestionsCache {
    private final RedisTemplate<String, List<String>> redisTemplate;
    private final HashOperations hashOperations; //to access Redis cache

    public RedisRepository(RedisTemplate<String, List<String>> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(String userId, Link link) {
        hashOperations.put("USER",userId, link);
    }

    @Override
    public Link findById(String userId) {
        return (Link) hashOperations.get("USER", userId);
    }

    @Override
    public void delete(String userId) {
        hashOperations.delete("USER", userId);
    }
}
