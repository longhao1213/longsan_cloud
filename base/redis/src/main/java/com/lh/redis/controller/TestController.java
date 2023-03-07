package com.lh.redis.controller;

import com.lh.redis.config.StreamRedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author longhao
 * @since 2023/3/7
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final StreamRedisUtil streamRedisUtil;

    @GetMapping("/send")
    public void send() {
        Map<String, Object> message = new HashMap<>();
        message.put("test","hello redismq");
        RecordId mystream = streamRedisUtil.addStream("mystream", message);
        System.out.println(mystream.toString());
    }
}
