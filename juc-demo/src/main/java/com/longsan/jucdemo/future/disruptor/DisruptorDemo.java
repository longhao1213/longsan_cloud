package com.longsan.jucdemo.future.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.longsan.jucdemo.future.disruptor.consumer.OrderEventHandler;
import com.longsan.jucdemo.future.disruptor.event.OrderEvent;
import com.longsan.jucdemo.future.disruptor.producer.OrderEventProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;

@Slf4j
public class DisruptorDemo {

    public static void main(String[] args) {
        // 创建disruptor
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024*1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy()//等待策略
        );

        // 设置消费者
        disruptor.handleEventsWith(new OrderEventHandler());
        //设置多消费者,消息会被重复消费
        //disruptor.handleEventsWith(new OrderEventHandler(),new OrderEventHandler());
        //设置多消费者,消费者要实现WorkHandler接口，一条消息只会被一个消费者消费
        //disruptor.handleEventsWithWorkerPool(new OrderEventHandler(), new OrderEventHandler());

        // 启动disruptor
        disruptor.start();

        // 创建RingBuffer容器
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        // 创建生产者
        OrderEventProducer eventProducer = new OrderEventProducer(ringBuffer);
        // 发送消息
        for (int i = 0; i < 100; i++) {
            eventProducer.onData(i,"longsan"+i);
        }
        disruptor.shutdown();
    }
}
