package rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.channel.ChannelHandlerContext;

public class RpcContainer {

    public static volatile List<RpcRequest> rpcRequestList = new ArrayList<>();
    public static volatile List<RpcResponse> rpcResponseList = new ArrayList<>();
    public static volatile AtomicBoolean isActive = new AtomicBoolean(false);
    public static volatile ConcurrentHashMap<Integer, ChannelHandlerContext>  channelHandlerContext = new ConcurrentHashMap<>();
}
