package rpc;

import static rpc.RpcContainer.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author nnzhang
 */
public class RpcProxyHandler implements InvocationHandler {
    private String fullClassName;

    static {
        initialNettyClient();
    }

    public RpcProxyHandler(String fullClassName) {
        this.fullClassName = fullClassName;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(!isActive.get()) {
            System.out.println("链路还没有建立，等待1000毫秒");
            Thread.sleep(1000);
        }
        RpcRequest request = getRpcRequest(method, args);
        return RpcClientHandler.send(request);

    }

    private RpcRequest getRpcRequest(Method method, Object[] args) {
        Class<?>[] params =  method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        RpcRequest request = new RpcRequest();
        request.setFullClassName(fullClassName);
        request.setMethodName(method.getName());
        request.setArgs(args);
        request.setParams(params);
        request.setReturnType(returnType);
        request.setRequestId(UUID.randomUUID().toString());

        return request;
    }

    private static void initialNettyClient() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcClientHandler());

                    }
                });

        connect(bootstrap, 8000);
    }

    private static void connect(final Bootstrap bootstrap, final int port) {
        bootstrap.connect("localhost", port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接成功");
                } else {
                    System.out.println("连接失败,将进行重新连接");
//                    connect(bootstrap, port);
                }
            }
        });

    }

}
