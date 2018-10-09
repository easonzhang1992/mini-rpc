package rpc;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nnzhang
 */
public class RpcUtil {

    private static Map<Class, Object> map = new ConcurrentHashMap<>();

    static {
        RpcProxyHandler rp = new RpcProxyHandler(UserServiceImpl.class.getName());
        IUserService proxyService = (IUserService)Proxy.newProxyInstance(RpcClient.class.getClassLoader(), UserServiceImpl.class.getInterfaces(), rp);
        map.put(IUserService.class, proxyService);
    }

    public static <T> T getService(Class<T> clazz) {
         return clazz.cast(map.get(clazz));
    }
}
