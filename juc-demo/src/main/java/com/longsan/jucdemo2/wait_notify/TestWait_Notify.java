package com.longsan.jucdemo2.wait_notify;

import java.util.concurrent.TimeUnit;

public class TestWait_Notify {

    private static Express express = new Express(0, "南充");

    /**
     * 检查公里数变化的线程，不满足条件，就一直等待
     */
    private static class CheckKm extends Thread{
        @Override
        public void run() {
            express.waitKm();
        }
    }

    /**
     * 检查目的地的线程，不满足条件，就一直等待
     */
    private static class CheckSite extends Thread {
        @Override
        public void run() {
            express.waitSite();
        }
    }

    /**
     * 主线程，循环5次来修改公里数，模拟快递运输
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            new CheckKm().start();
        }
        for (int i = 0; i < 2; i++) {
            new CheckSite().start();
        }

        TimeUnit.MILLISECONDS.sleep(500);

        for (int i = 0; i < 5; i++) {
            synchronized (express) {
                express.change();
                express.notifyAll();
            }
            TimeUnit.MILLISECONDS.sleep(500);
        }

    }

}
