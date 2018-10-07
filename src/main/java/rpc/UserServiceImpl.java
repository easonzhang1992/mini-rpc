package rpc;

/**
 * @author nnzhang
 */
public class UserServiceImpl implements IUserService {
    @Override
    public String sayHello(String name) {

        return "hello, " + name;

    }
}
