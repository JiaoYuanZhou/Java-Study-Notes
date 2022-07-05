import dao.User;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class RPCClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //建立连接
        Socket socket = new Socket("localhost",8899);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        //写入id
        objectOutputStream.writeInt(new Random().nextInt());

        //清空缓存
        objectOutputStream.flush();

        //获取服务端传过来的User对象
        User user= (User)objectInputStream.readObject();

        System.out.println("服务端传过来的对象为：" + user);

    }
}
