package com.longsan.jucdemo.jmm.sync;

import com.longsan.jucdemo.jmm.ObjectTest;
import org.openjdk.jol.info.ClassLayout;

public class LockEscalationDemo {

    /**
     * 在线程竞争不太激烈的时候，为了降低锁开销，使用偏向锁
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {
//        Thread.sleep(5000);
        Object obj = new ObjectTest.Test();
//        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        new Thread(()->{
            synchronized (obj) {
                // 轻量级锁
                System.out.println(Thread.currentThread().getName() + "\n" +
                        ClassLayout.parseInstance(obj).toPrintable());
            }

        },"thread2").start();
        // 休眠1秒，防止竞争太激烈
        Thread.sleep(1000L);
        new Thread(()->{
            synchronized (obj) {
                // 轻量级锁
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
