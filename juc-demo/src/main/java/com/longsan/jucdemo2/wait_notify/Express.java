package com.longsan.jucdemo2.wait_notify;

/**
 * 快递实体类
 */
public class Express {
    // 目的城市
    public final static String DIST_CITY = "成都";
    // 总里程
    public final static int TOTAL = 500;
    // 当前运输公里数
    private int km;
    // 快递地点
    private String site;

    public Express(){}

    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    // 改变快递状态的方法
    public void change(){
        if (km < TOTAL) {
            km = km + 100;
            System.out.println("当前公里数：" + km);
        }
        if (km >= TOTAL) {
            site = DIST_CITY;
            System.out.println("快递已经到达");
        }
    }

    /**
     * 等待公里数的变换
     */
    public synchronized void waitKm() {
        while (this.km <= TOTAL) {
            try {
                wait();
                System.out.println("地图线程【" + Thread.currentThread().getId() + "】，继续等待公里数变化");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 等待目的地的变化，通知用户
     */
    public synchronized void waitSite(){
        while (!this.site.equals(DIST_CITY)) {
            try {
                wait();
                System.out.println("地址线程【" + Thread.currentThread().getId() + "】，等待到达目的地");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("快递已经到达成都，通知用户");
    }


}
