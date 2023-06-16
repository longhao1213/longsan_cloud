package com.longsan.jucdemo.future.disruptor.consumer;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.longsan.jucdemo.future.disruptor.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBathc) throws Exception {
        // 消费逻辑
        log.info("消费者：{},获取数据value:{},name:{}",Thread.currentThread().getName(),orderEvent.getValue(),orderEvent.getName());
    }

    @Override
    public void onEvent(OrderEvent orderEvent) throws Exception {
        // 消费逻辑
        log.info("消费者：{},获取数据value:{},name:{}",Thread.currentThread().getName(),orderEvent.getValue(),orderEvent.getName());

    }
}
