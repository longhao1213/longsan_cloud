package com.longsan.jucdemo.jmm.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        // 声明3个窗口 3个信号量
        Semaphore windows = new Semaphore(3);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    // 占用一个窗口 信号量
                    windows.acquire();
                    System.out.println(Thread.currentThread().getName() + "开始买票");
                    // 模拟买票流程
                    Thread.sleep(5000L);
                    System.out.println(Thread.currentThread().getName() + "购票成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    windows.release();
                }
            },"thread"+i).start();
        }

    }


}
