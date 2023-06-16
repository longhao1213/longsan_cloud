package com.longsan.jucdemo.blockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author Fox
 */
public class BlockingQueueDemo {

    final static BlockingQueue<Integer> blockingQueue = new SynchronousQueue<>();

    public static void main(String[] args) throws InterruptedException {

        //TODO  模拟消费者取数据
        new Thread(()->{take();},"consumer1").start();
        // 控制第一个消费者先调用
        Thread.sleep(10);
        new Thread(()->{take();},"consumer2").start();

        Thread.sleep(100);

        //TODO  模拟生产者写数据
        new Thread(()->{put(1);},"producer1").start();
        // 控制第一个生产者先调用
        Thread.sleep(10);
        new Thread(()->{put(5);},"producer2").start();


    }

    public static void take(){
        try {
            Integer item = blockingQueue.take();
            System.out.println(Thread.currentThread().getName() + " Take: " + item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void put(Integer item){
        try {
            blockingQueue.put(item);
            System.out.println(Thread.currentThread().getName() + " Put: " + item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




}
