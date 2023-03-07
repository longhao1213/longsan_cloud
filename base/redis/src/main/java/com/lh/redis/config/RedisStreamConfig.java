package com.lh.redis.config;

import com.lh.redis.stream.ListenerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author longhao
 * @since 2023/3/7
 */
@Configuration
public class RedisStreamConfig {

    @Autowired
    private ListenerMessage listenerMessage;

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public Subscription subscription(RedisConnectionFactory factory) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofSeconds(1))
                .build();
        initStream("mystream", "mygroup");
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer = StreamMessageListenerContainer.create(factory, options);
        Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from("mygroup", "longhao"),
                StreamOffset.create("mystream", ReadOffset.lastConsumed()), listenerMessage);
        listenerContainer.start();
        return subscription;
    }

    private void initStream(String key, String group) {
        Boolean aBoolean = redisTemplate.hasKey(key);
        Boolean hasKey = aBoolean == null ? false : aBoolean;
        if (!hasKey) {
            Map<String, Object> map = new HashMap<>();
            map.put("field", "value");
            RecordId recordId = redisTemplate.opsForStream().add(key, map);
            redisTemplate.opsForStream().createGroup(key, group);
            //将初始化的值删除掉
            redisTemplate.opsForStream().delete(key, recordId.getValue());
        }
    }
}
