package com.longsan.jucdemo.jmm.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * ABA问题
 * 解决方案就是加版本号
 * 可以使用AtomicStampedReference
 */
@Slf4j
public class ABATest {

    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(1);

        new Thread(() -> {
            int value = atomicInteger.get();
            log.debug("线程1读取value为{}", value);

            // 阻塞1秒
            LockSupport.parkNanos(1000000000L);

            // 线程1通过cas修改value为3
            if (atomicInteger.compareAndSet(value, 3)) {
                log.debug("线程1修改value从{}到3", value);
            } else {
                log.debug("线程1修改失败");
            }
        },"thread1").start();

        new Thread(() -> {
            int value = atomicInteger.get();
            log.debug("线程2读取value为{}", value);

            // 线程2通过cas修改value为2
            if (atomicInteger.compareAndSet(value, 2)) {
                log.debug("线程2修改value从{}到2", value);

                // 做某些操作
                value = atomicInteger.get();
                log.debug("线程2读取value为{}", value);
                // 线程2通过cas修改value为1
                if (atomicInteger.compareAndSet(value, 1)) {
                    log.debug("线程2修改value从{}到1", value);
                }
            }
        }, "thread2").start();
    }
}
