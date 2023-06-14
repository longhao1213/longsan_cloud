package com.longsan.jucdemo.jmm.readWriteLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class ReadWriteLockTest2 {

    private Object data;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock r = readWriteLock.readLock();
    private Lock w = readWriteLock.writeLock();

    private void read() {
        log.debug("准备获取到读锁");
        r.lock();
        try {
            log.debug("获取到读锁，读取数据。。。");
            data = "read";
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r.unlock();
            log.debug("释放读锁");
        }
    }

    private void write() {
        log.debug("准备获取到写锁");
        w.lock();
        try {
            log.debug("获取到写锁，写入数据。。。");
            data = "write";
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            w.unlock();
            log.debug("释放写锁");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockTest2 test2 = new ReadWriteLockTest2();
        // 测试读读、读写、写读、写写场景
        new Thread(test2::read, "thread1").start();

        Thread.sleep(10);

        new Thread(test2::write, "thread2").start();
    }
}
