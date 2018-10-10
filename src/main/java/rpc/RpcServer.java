package rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class RpcServer {
    public static void main(String[] args){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();  //监听端口，接收新的请求，默认设置为CPU的核数 * 2
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(); //工作线程，处于处理请求

        ServerBootstrap serverBootstrap = new ServerBootstrap(); //引导类，用于服务端的启动工作
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                // childHandler用于处理新连接线程的读写逻辑

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //如果有一个新的连接过来并且建立连接成功，就会走到这里
                        System.out.println("接收到连接");
                        ch.pipeline().addLast(new RpcServerHandler());

                    }
                }).option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);

        bind(serverBootstrap, 8000); //端口绑定是异步的，可以添加Listener来监听异步通知的结果，以方便后续处理逻辑
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口：" + port + "绑定成功");
                } else {
                    System.out.println("端口：" + port + "绑定失败");
                }
            }
        });
    }
}
