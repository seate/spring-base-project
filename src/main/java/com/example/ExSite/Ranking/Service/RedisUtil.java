package com.example.ExSite.Ranking.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;
    private final String keyword = "searchRanking";

    public void addScore(String searchWord) {
        if (Boolean.TRUE.equals(redisTemplate.opsForZSet().addIfAbsent(keyword, searchWord, 1))){
            redisTemplate.opsForZSet().incrementScore(keyword, searchWord, 1);
        }
    }

    public Set<String> getRanking() {
        return redisTemplate.opsForZSet().reverseRange(keyword, 0, 10);
    }
}
