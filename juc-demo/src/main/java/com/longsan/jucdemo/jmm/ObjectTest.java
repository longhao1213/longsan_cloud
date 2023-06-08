package com.longsan.jucdemo.jmm;

import org.openjdk.jol.info.ClassLayout;

public class ObjectTest {

    /**
     * -XX:BiasedLockingStartupDelay=0 关闭偏向锁延迟  在jdk15版本中弃用
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 默认情况下 jvm启动4秒后开始启用偏向锁
        Thread.sleep(4000L);
        Object obj = new Test();
        // 查看对象内部信息
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        new Thread(()->{
            synchronized (obj) {
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
