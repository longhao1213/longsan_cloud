package com.longsan.jucdemo2;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class FiberExample {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(10000);
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        IntStream.range(0,10000).forEach(i -> new Fiber(){

            @Override
            protected String run() throws SuspendExecution, InterruptedException {
                Strand.sleep(1000);
                count.countDown();
                return "aa";
            }
        }.start());
        count.await();
        stopwatch.stop();
        System.out.println("结束" + stopwatch.prettyPrint());
    }
}
