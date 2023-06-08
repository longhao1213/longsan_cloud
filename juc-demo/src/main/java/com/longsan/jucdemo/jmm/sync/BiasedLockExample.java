package com.longsan.jucdemo.jmm.sync;

import org.openjdk.jol.info.ClassLayout;

public class BiasedLockExample {
    public static void main(String[] args) {
        Object obj = new Object();
        synchronized (obj) {
            //  偏向锁
            System.out.println(Thread.currentThread().getName() + "\n" +
                    ClassLayout.parseInstance(obj).toPrintable());
        }

        synchronized (obj) {
            // 偏向锁
            System.out.println(Thread.currentThread().getName() + "\n" +
                    ClassLayout.parseInstance(obj).toPrintable());
        }
    }

}
