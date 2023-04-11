package com.longsan.jucdemo.jmm;

import java.util.concurrent.locks.ReentrantLock;

public class Test {

    private volatile static int sum = 0;
    static Object object = "";
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
//                synchronized (object) {
                lock.lock();
                try {
                    for (int j = 0; j < 10000; j++) {
                        sum++;
                    }
                } finally {
                    lock.unlock();
                }

//                }

            });
            thread.start();
        }
        try {
            Thread.sleep(3000);
            System.out.println(sum);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
