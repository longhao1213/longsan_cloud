package com.longsan.jucdemo2;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class OnlyMain {

    public static void main(String[] args) {
        // 获取线程管理器
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 获取所有的线程
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println(threadInfo.getThreadId() + "-" + threadInfo.getThreadName());
        }
    }
}
