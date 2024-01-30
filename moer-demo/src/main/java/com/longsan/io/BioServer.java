package com.longsan.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 同步阻塞IO模型
 * socket 不同的计算机节点之间通信的端点（Endpoint）的抽象
 */
public class BioServer {

    public static void main(String[] args) {
        // 开启一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            // 启动服务，绑定8080端口
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8080));
            System.out.println("开启服务");

            while (true) {
                System.out.println("等待客户端建立连接");
                // 监听8080端口，获取客户端连接
                Socket socket = serverSocket.accept(); // 阻塞
                System.out.println("建立连接："+socket);

                executorService.submit(()->{
                    // 处理业务
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 资源回收
        }
    }

    private static void handler(Socket socket) throws IOException {
        while (true) {
            byte[] bytes = new byte[1024];
            System.out.println("等待读取数据");
            int read = socket.getInputStream().read(bytes); // 阻塞
            if (read != -1) {
                System.out.println("读取客户端发送的数据：" + new String(bytes, 0, read));
            } else {
                break;
            }
        }
    }
}
