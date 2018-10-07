package rpc;

import org.apache.commons.lang3.SerializationUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RpcSender {


    public static void send(RpcRequest request) {
        if(RpcContainer.isActive.get()) {
            ChannelHandlerContext ctx = RpcContainer.channelHandlerContext.get(0);
            send1(request, ctx);

        }
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
}
