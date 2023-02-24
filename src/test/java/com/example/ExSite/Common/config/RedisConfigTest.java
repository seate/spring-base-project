package com.example.ExSite.Common.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class RedisConfigTest {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private final String keyword = "tempKeyWord";

    @Test
    void connectionTest() {
        String word1 = "1번 단어";
        String word2 = "2번 단어";


        redisTemplate.opsForZSet().add(keyword, word1, 1);
        redisTemplate.opsForZSet().incrementScore(keyword, word1, 1);
        redisTemplate.opsForZSet().incrementScore(keyword, word1, 3);

        redisTemplate.opsForZSet().add(keyword, word2, 2);

        System.out.println(redisTemplate.opsForZSet().popMax(keyword));
        System.out.println(redisTemplate.opsForZSet().popMin(keyword));
    }
}