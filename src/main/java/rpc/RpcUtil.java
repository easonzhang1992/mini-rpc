package rpc;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcUtil {

    private static Map<String, Object> map = new ConcurrentHashMap<>();

    static {
        IUserService service = new UserServiceImpl();
        RpcProxyHandler rp = new RpcProxyHandler(service);
        IUserService proxyService = (IUserService)Proxy.newProxyInstance(RpcClient.class.getClassLoader(), service.getClass().getInterfaces(), rp);
        map.put("IUserService", proxyService);
    }

    public static Object getService(String serviceName) {
        Object service =  map.get(serviceName);
        return service;
    }
}
