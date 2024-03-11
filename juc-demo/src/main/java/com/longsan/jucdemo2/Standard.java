package com.longsan.jucdemo2;


import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Standard {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(10000);
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        IntStream.range(0,10000).forEach(i -> executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                count.countDown();
            }
        }));
        count.await();
        stopwatch.stop();
        System.out.println("结束" + stopwatch.prettyPrint());
    }
}
