package com.longsan.jucdemo.jmm;

/**
 * @author longhao
 * @since 2023/2/28
 * 线程 可见性问题
 */
public class VisibilityTest {

    /**
     * 默认情况下，执行后线程A是无法跳出循环的，因为flag这个值被修改后在线程a不可见
     *
     * 解决的根本就是让线程本地缓存中的值被淘汰，让线程去从主内存中获取值
     *
     * 解决方案有很多种
     * 1：在flag加上volatile关键字，保存这个变量在线程修改后其他线程可见 本质上是添加了一个内存屏障 --->（汇编层面指令）lock; addl $0,0(%%rsp)  lock指令会让所有的缓冲的写操作写会内存，并且让其他的本地缓冲失效
     * 6: 给count加上volatile关键字
     * 7：给count的类型改成Integer 里面会用到final关键字，final也可以保证内存可见性
     */

    private boolean flag = true;
    private int count = 0;

    public void load() {
        System.out.println(Thread.currentThread().getName() + "开始执行.....");
        while (flag) {
            count++;

            // 2：使用内存屏障
//            Unsafe.getUnsafe().storeFence();
            // 3:使用yield（）方法 ？？ 会释放时间片，进行上下文切换 会从新加载上下文，就可以读取到新的flag值
//            Thread.yield();
            // 4:打印count
//            System.out.println(count);
            // 5:发放线程许可
//            LockSupport.unpark(Thread.currentThread());
            // 8：循环时间延长1毫秒
            shortWait( 1000000);

        }
        System.out.println(Thread.currentThread().getName() + "跳出循环：count=" + count);
    }

    public void refresh() {
        flag = false;
        System.out.println(Thread.currentThread().getName() + "修改flag：" + flag);
    }

    public static void main(String[] args) throws InterruptedException {
        VisibilityTest test = new VisibilityTest();

        // 线程threadA模拟数据加载场景
        Thread threadA = new Thread(() -> test.load(), "threadA");
        threadA.start();

        // 让threadA执行一会
        Thread.sleep(1000);
        // 线程threadB通过flag控制threadA的执行时间
        Thread threadB = new Thread(() -> test.refresh(), "threadB");
        threadB.start();
    }

    /**
     * 循环一段时间 单位纳秒 很短暂
     * @param interval
     */
    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}
