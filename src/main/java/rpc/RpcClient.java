package rpc;

/**
 * @author  nnzhang
 */
public class RpcClient {

    public static void main(String[] args) {
        IUserService userService = RpcUtil.getService(IUserService.class);

        String result = userService.sayHello("nnzhang");
        System.out.println("the result = " + result);
    }
}
