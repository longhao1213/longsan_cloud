package com.longsan.jucdemo.jmm.sync;

import org.openjdk.jol.info.ClassLayout;

public class HeavyweightLockExample {

    /**
     * 当线程对同一个资源竞争比较激烈对时候，synchronized锁就会变成重量级锁状态
     */

    private static final Object lock = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Worker(),"thread1");
        Thread thread2 = new Thread(new Worker(),"thread2");

        thread1.start();
        thread2.start();
    }

    private static class Worker implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " 获取锁");
                System.out.println(Thread.currentThread().getName() + "\n" +
                        ClassLayout.parseInstance(lock).toPrintable());
                try {
                    Thread.sleep(2000); // 模拟持有锁的操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
