package com.longsan.jucdemo.future.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import com.longsan.jucdemo.future.disruptor.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息生产者
 */
@Slf4j
public class OrderEventProducer {

    // 事件队列
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long value, String name) {
        // 获取事件队列的下一个槽
        long sequence = ringBuffer.next();
        try {
            // 获取消息（事件）
            OrderEvent orderEvent = ringBuffer.get(sequence);
            // 写入消息数据
            orderEvent.setValue(value);
            orderEvent.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            log.info("生产者：{},发送数据value：{},name:{}",Thread.currentThread().getName(),value,name);
            //发布事件
            ringBuffer.publish(sequence);
        }
    }
}
