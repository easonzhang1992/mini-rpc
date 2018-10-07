package rpc;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcRequest request = getRequestObject(msg);

        String fullClassName = request.getFullClassName();
        String methodName = request.getMethodName();
        Object[] args = request.getArgs();
        Class<?>[] params = request.getParams();
        Class<?> returnType = request.getReturnType();

        Class clz = Class.forName(fullClassName);
        Object obj= clz.newInstance();
        Method method = clz.getMethod(methodName, params);
        Object result = method.invoke(obj, args);

        System.out.println("the result = " + result);
//        System.out.println("the result = " + returnType.cast(result));

        byte[] bytes = JSON.toJSONBytes(result);
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes(bytes);
        ctx.channel().writeAndFlush(byteBuf);

    }

    private RpcRequest getRequestObject(Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes); //将数据传输到bytes字节数组中
        RpcRequest request = SerializationUtils.deserialize(bytes);

        System.out.println(new Date() + ": 服务端读到数据 -> " + request.toString());

        return request;
    }


}
