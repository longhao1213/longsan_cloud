package com.longsan.jucdemo.future.disruptor.event;

import lombok.Data;

/**
 * 消息载体
 */
@Data
public class OrderEvent {
    private long value;
    private String name;
}
