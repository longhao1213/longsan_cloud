package com.longsan.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 作者：Mark
 * 类说明：基于Netty的服务器
 */

public class EchoServer  {
    private static final Logger LOG = LoggerFactory.getLogger(EchoServer.class);

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        EchoServer echoServer = new EchoServer(port);
        LOG.info("服务器即将启动");
        echoServer.start();
        LOG.info("服务器关闭");
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        /*线程组*/
        EventLoopGroup group  = new NioEventLoopGroup();
        try {
            /*服务端启动必备*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
            .channel(NioServerSocketChannel.class)/*指定使用NIO的通信模式*/
            .localAddress(new InetSocketAddress(port))/*指定监听端口*/
                    //.childOption()
            //.handler();
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(serverHandler);
                }
            });
            ChannelFuture f = b.bind().sync();/*异步绑定到服务器，sync()会阻塞到完成*/
            f.channel().closeFuture().sync();/*阻塞当前线程，直到服务器的ServerChannel被关闭*/
        } finally {
            group.shutdownGracefully().sync();

        }


    }


}
