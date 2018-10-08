package rpc;

import java.lang.reflect.Proxy;

/**
 * @author  nnzhang
 */
public class RpcClient {

    public static void main(String[] args) {
        IUserService userService = (IUserService)RpcUtil.getService("IUserService");

        String result = userService.sayHello("nnzhang");
        System.out.println("the result = " + result);
    }
}
