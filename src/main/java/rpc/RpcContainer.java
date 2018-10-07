package rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.ChannelHandlerContext;

public class RpcContainer {

    public static volatile Map<String, RpcResponse> rpcResponseMap = new ConcurrentHashMap<>();
    public static volatile AtomicBoolean isActive = new AtomicBoolean(false);
    public static volatile ConcurrentHashMap<Integer, ChannelHandlerContext>  channelHandlerContext = new ConcurrentHashMap<>();
    public static volatile ReentrantLock lock = new ReentrantLock();
    public static volatile Condition respCondition = lock.newCondition();


}
