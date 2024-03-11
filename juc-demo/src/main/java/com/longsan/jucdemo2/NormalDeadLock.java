package com.longsan.jucdemo2;

/**
 * 模拟死锁
 */
public class NormalDeadLock {

    // 先要创建两把锁
    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    // 第一个拿锁的方法
    private static void getLock1() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        synchronized (lock1) {
            System.out.println(threadName + "拿到了lock1");
            Thread.sleep(100);
            synchronized (lock2) {
                System.out.println(threadName + "拿到了lock2");
            }
        }
    }

    // 第2个拿锁的方法
    private static void getLock2() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        synchronized (lock2) {
            System.out.println(threadName + "拿到了lock2");
            Thread.sleep(100);
            synchronized (lock1) {
                System.out.println(threadName + "拿到了lock1");
            }
        }
    }

    /**
     * 子线程
     */
    private static class ChildThread extends Thread{
        @Override
        public void run() {
            Thread.currentThread().setName("childThread");
            try {
                getLock1();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ChildThread childThread = new ChildThread();
        childThread.start();

        getLock2();
    }
}
