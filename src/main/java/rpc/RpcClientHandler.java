package rpc;

import static rpc.RpcContainer.channelHandlerContext;
import static rpc.RpcContainer.isActive;
import static rpc.RpcContainer.rpcResponseList;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        isActive.set(true);
        channelHandlerContext.putIfAbsent(0, ctx);




    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到服务端的应答：" +  byteBuf.toString(Charset.forName("UTF-8")));

        RpcResponse response = getResponse(byteBuf);
        System.out.println("the response is " + response);
        rpcResponseList.add(response);

    }

    private RpcResponse getResponse(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object data = JSON.parse(bytes);
        RpcResponse response = new RpcResponse();
        response.setResponseData(data);
        return response;
    }

}
