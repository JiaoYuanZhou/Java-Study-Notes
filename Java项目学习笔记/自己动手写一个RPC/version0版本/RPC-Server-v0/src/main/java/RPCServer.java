import dao.User;
import service.UserService;
import service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        try {
            ServerSocket  serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动啦....");

        while(true) {
            Socket socket = serverSocket.accept();
            new Thread(()-> {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    // 读取客户端传过来的id
                    Integer id = ois.readInt();
                    User userByUserId = userService.getUserById(id);
                    // 写入User对象给客户端
                    oos.writeObject(userByUserId);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("从IO中读取数据错误");
                }
            }).start();
        }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
