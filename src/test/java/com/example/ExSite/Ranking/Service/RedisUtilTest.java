package com.example.ExSite.Ranking.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisUtilTest {

    private final RedisUtil redisUtil;

    @Autowired
    RedisUtilTest(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Test
    void ddd() {
        String wo1 = "qwe";
        String wo2 = "ww";


        redisUtil.addScore(wo1);
        redisUtil.addScore(wo1);
        redisUtil.addScore(wo1);
        redisUtil.addScore(wo1);

        redisUtil.addScore(wo2);
        redisUtil.addScore(wo2);
        redisUtil.addScore(wo2);
        redisUtil.addScore(wo2);
        redisUtil.addScore(wo2);
        redisUtil.addScore(wo2);

        redisUtil.getRanking();
    }

}