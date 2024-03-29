package com.lh.redis.stream;

import com.lh.redis.config.StreamRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

/**
 * redis stream监听消息
 */
@Component
public class ListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {

    @Autowired
    private StreamRedisUtil streamRedisUtil;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        System.out.println("接受到消息");
        // 接收到消息
        System.out.println("message id " + message.getId());
        System.out.println("stream " + message.getStream());
        System.out.println("body " + message.getValue());
//        streamRedisUtil.delField(message.getStream(), message.getId().toString());

        System.out.println("删除消息");
    }

}