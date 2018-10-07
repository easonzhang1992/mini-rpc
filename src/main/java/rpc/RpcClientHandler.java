package rpc;

import static rpc.RpcContainer.*;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SerializationUtils;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        isActive.set(true);
        channelHandlerContext.putIfAbsent(0, ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(new Date() + "收到服务端的应答：" + byteBuf.toString(Charset.forName("UTF-8")));

            lock.lock();
            RpcResponse response = parseNettyResponse(byteBuf);
            System.out.println("the response is " + response);
            rpcResponseMap.put(response.getRequestId(), response);
            System.out.println(new Date() + "收到应答，锁的状态：" + lock.isLocked());
            respCondition.signal();
        }finally {
            lock.unlock();
            System.out.println(new Date() + "收到应答，释放锁");
        }
    }

    private RpcResponse parseNettyResponse(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        RpcResponse response = JSON.parseObject(new String(bytes, CharsetUtil.UTF_8), RpcResponse.class);
//        RpcResponse response = (RpcResponse)JSON.parse(bytes);

        return response;
    }

    public static Object send(RpcRequest request) {
        if(RpcContainer.isActive.get()) {
            ChannelHandlerContext ctx = channelHandlerContext.get(0);
            send1(request, ctx);

        }
        try {
            lock.lock();
            System.out.println(new Date() + "发送消息时锁的状态：" + lock.isLocked());
            while(!rpcResponseMap.containsKey(request.getRequestId())) {
                System.out.println("请求还没有得到响应，将等待2秒钟");

                // 此处可以设置超时时间，该参数可以放在配置文件中进行配置
                boolean istimely = respCondition.await(2000, TimeUnit.MILLISECONDS);
                if(!istimely) {
                    System.out.println("超时了，此处应该抛出异常...");
                }
            }
            return getResponseData(request.getRequestId());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            System.out.println(new Date() + "发送消息时释放锁");
        }
        return null;
    }

    private static Object getResponseData(String requestId) {
        RpcResponse response = rpcResponseMap.get(requestId);
        rpcResponseMap.remove(requestId);
        return response.getResponseData();
    }

    private static void send1(RpcRequest request, ChannelHandlerContext ctx) {
        ByteBuf buf = getByteBuffer(request, ctx);
        ctx.channel().writeAndFlush(buf);
    }

    private static ByteBuf getByteBuffer(RpcRequest request, ChannelHandlerContext ctx) {
        byte[] bytes = SerializationUtils.serialize(request);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }

   /* @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("捕捉到异常：" + cause);
    }*/

}
