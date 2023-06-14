package com.longsan.jucdemo.jmm.aqs;

public class ReentrantLockDemo1 {

    static int sum;
    static MyLock lock = new MyLock();
//    ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 3; j++) {

            Thread thread = new Thread(() -> {
                lock.lock();
                try {
                    // 临界区代码
                    for (int i = 0; i < 10000; i++) {
                        sum++;
                    }
                } finally {
                    lock.unLock();
                }
            });
            thread.start();
        }
        Thread.sleep(1000L);
        System.out.println(sum);
    }
}
