
import config.ClientProxy;
import dao.User;
import service.UserService;


public class RPCClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1",8899);

        UserService proxy = clientProxy.getProxy(UserService.class);

        User userByUserId = proxy.getUserByUserId(2);
        System.out.println("通过id得到的user" + userByUserId);

        User jyz = User.builder().id(5)
                .sex(true)
                .userName("jyz").build();
        Integer integer = proxy.insertUserId(jyz);

    }
}
