import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端接受并解析 request与封装发生response对象
 * version1.0改进：version0版本客户端只能传一个id过来，目前客户端可以通过RPCRequest对象传想要调用的
 * 接口名，方法名，参数列表，参数类型列表过来
 * 服务端通过反射来调用方法返回结果给客户端
 */
public class RPCService {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动啦...");

            //以BIO的方式去监听Socket
            while(true) {
                Socket socket = serverSocket.accept();
                // 开启一个线程去处理，这个类负责的功能太复杂，以后代码重构中，这部分功能要分离出来
                new Thread(()-> {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        //读取客户端传送过来的request
                        RPCRequest request = (RPCRequest)ois.readObject();
                        //反射调用对应方法
                        Method method = userService.getClass().getMethod(request.getMethodName(),request.getParamsTypes());
                        Object invoke = method.invoke(userService, request.getParams());

                        //封装，写进response对象  ？？？将什么进行封装----->将invoke调用方法的返回值封装
                        oos.writeObject(RPCResponse.success(invoke));
                        oos.flush();

                    } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
