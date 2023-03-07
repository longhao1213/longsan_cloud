package com.redis.test;

import com.lh.redis.RedisApplication;
import com.lh.redis.config.StreamRedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author longhao
 * @since 2023/3/7
 */
@SpringBootTest(classes = RedisApplication.class)
public class RedisTest {

    @Resource
    private StreamRedisUtil streamRedisUtil;

    @Test
    public void test1() {
        Map<String, Object> message = new HashMap<>();
        message.put("test","hello redismq");
        message.put("send time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        redisTemplate.opsForStream().add("mystream", message);
//        redisTemplate.opsForStream().add("mystream", message);
        streamRedisUtil.addStream("mystream", message);
    }
}
