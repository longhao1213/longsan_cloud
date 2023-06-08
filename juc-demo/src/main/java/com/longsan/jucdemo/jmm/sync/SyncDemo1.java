package com.longsan.jucdemo.jmm.sync;

import com.longsan.jucdemo.jmm.ObjectTest;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j
public class SyncDemo1 {

    /**
     * -XX:BiasedLockingStartupDelay=0 关闭偏向锁延迟  在jdk15版本中弃用
     *
     * 在大多情况下 锁不存在多线程竞争，为了减小开销而引入了偏向锁。
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 默认情况下 jvm启动4秒后开始启用偏向锁
        Thread.sleep(4000L);
        Object obj = new ObjectTest.Test();
        // 查看对象内部信息
        // 此时也是偏向锁状态，但是不会记录线程id，被称作匿名偏向锁
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        new Thread(()->{
            synchronized (obj) {
                // 偏向锁
                System.out.println(Thread.currentThread().getName() + "\n" +
                        ClassLayout.parseInstance(obj).toPrintable());
            }

        },"thread1").start();
    }

    public static class Test {
        private boolean f;
        private long p;
    }

}
