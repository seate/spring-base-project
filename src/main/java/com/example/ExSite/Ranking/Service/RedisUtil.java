package com.example.ExSite.Ranking.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;
    private final String keyword = "searchRanking";

    public void addScore(String searchWord) {
        if (!redisTemplate.opsForZSet().addIfAbsent(keyword, searchWord, 1)){
            redisTemplate.opsForZSet().incrementScore(keyword, searchWord, 1);
        }
    }

    public void getRanking() {
        //TODO ranking return 고치기
        System.out.println(redisTemplate.opsForZSet().reverseRange(keyword, 0, 10));
    }
}
