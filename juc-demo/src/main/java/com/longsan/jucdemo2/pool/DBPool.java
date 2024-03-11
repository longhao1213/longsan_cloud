package com.longsan.jucdemo2.pool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 连接池
 */
public class DBPool {

    /**
     * 存放连接池的容器
     */
    private static LinkedList<Connection> pool = new LinkedList<>();

    /**
     * 限制连接池20
     */
    public DBPool(int initalSize) {
        if (initalSize > 0) {
            initalSize = Math.min(initalSize, 20);
            for (int i = 0; i < initalSize; i++) {
                pool.addLast(SqlConnectImpl.fetchConnection());
            }
        }
    }

    /**
     * 释放连接，通知其他等待连接的线程
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                pool.addLast(connection);
                // 通知其他线程
                pool.notifyAll();
            }
        }
    }

    /**
     * 获取连接
     */
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            // 用不超时
            if (mills <= 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                // 超时等待
                long future = System.currentTimeMillis() + mills;
                // 等待时长
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait();
                    remaining = future - System.currentTimeMillis();
                }
                Connection connection = null;
                if (!pool.isEmpty()) {
                    connection = pool.removeFirst();
                }
                return connection;
            }
        }
    }


}
