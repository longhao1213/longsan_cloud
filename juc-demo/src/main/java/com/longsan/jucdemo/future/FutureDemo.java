package com.longsan.jucdemo.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new Thread(() -> {
            log.info("通过Runnable方式执行任务");
        }).start();

        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                log.info("通过Callable执行任务");
                Thread.sleep(3000);
                return "返回任务结果";
            }
        });
        new Thread(task).start();

        log.info("结果：{}",task.get());
    }

}
