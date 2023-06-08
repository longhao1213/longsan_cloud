package com.longsan.jucdemo.jmm;

import com.longsan.jucdemo.jmm.lock.CASLock;

import java.util.concurrent.locks.ReentrantLock;

public class Test {

    private volatile static int sum = 0;
    static Object object = "";
    static ReentrantLock lock = new ReentrantLock();

    static CASLock casLock = new CASLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
//                synchronized (object) {
//                lock.lock();
                for (; ; ) {
                    if (casLock.getState() == 0 && casLock.cas()) {

                        try {
                            for (int j = 0; j < 10000; j++) {
                                sum++;
                            }
                        } finally {
//                    lock.unlock();
                            casLock.setState(0);

                        }
                        break;
                    }

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
