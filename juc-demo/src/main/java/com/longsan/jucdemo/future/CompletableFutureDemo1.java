package com.longsan.jucdemo.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runnable runnable = () -> System.out.println("执行无返回结果的异步任务");
        CompletableFuture.runAsync(runnable);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行有返回结果的异步任务");
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hello world";
        });
        String result = future.get();
        System.out.println(result);
    }
}
