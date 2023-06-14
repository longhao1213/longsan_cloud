package com.longsan.jucdemo.jmm.cyclicBarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Fox
 */
@Slf4j
public class CyclicBarrierTest3 {

    public static void main(String[] args) {

        AtomicInteger counter = new AtomicInteger();
        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 5, 1000, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                (r) -> new Thread(r, counter.addAndGet(1) + " 号 "),
                new ThreadPoolExecutor.AbortPolicy());

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,
                () -> System.out.println("裁判：比赛开始~~"));

        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(new Runner(cyclicBarrier));
        }

    }
    static class Runner extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Runner (CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                int sleepMills = ThreadLocalRandom.current().nextInt(1000);
                Thread.sleep(sleepMills);
                System.out.println(Thread.currentThread().getName()
                        + " 选手已就位, 准备共用时： " + sleepMills + "ms"
                        + cyclicBarrier.getNumberWaiting());

                // 指定线程数是5
                cyclicBarrier.await();

                System.out.println(Thread.currentThread().getName()+"比赛进行中...");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
        }
    }

}
