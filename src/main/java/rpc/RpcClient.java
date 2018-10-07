package rpc;

import java.lang.reflect.Proxy;

/**
 * @author  nnzhang
 */
public class RpcClient {

    public static void main(String[] args) {
        IUserService service = new UserServiceImpl();

        RpcProxyHandler rp = new RpcProxyHandler(service);
        IUserService proxyService = (IUserService)Proxy.newProxyInstance(RpcClient.class.getClassLoader(), service.getClass().getInterfaces(), rp);


        String result = proxyService.sayHello("nnzhang");
        System.out.println("the result = " + result);
    }
}
