package com.rpc.test;

import org.junit.Test;

import rpc.IUserService;
import rpc.RpcUtil;

public class RpcClientTest {

    @Test
    public void rpcTest() {
        IUserService userService = RpcUtil.getService(IUserService.class);
        for(int i=0; i<1000000000; i++) {
            long begin = System.currentTimeMillis();
            String result = userService.sayHello("nnzhang");
//        System.out.println("the result = " + result);

            System.out.println("cost time=" + (System.currentTimeMillis() - begin) + "ms");
        }
    }

}
