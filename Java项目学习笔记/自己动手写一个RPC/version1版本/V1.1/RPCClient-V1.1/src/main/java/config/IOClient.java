package config;

import R.RPCRequest;
import R.RPCResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
    // 这里负责底层与服务端的通信，发送的Request，接受的是Response对象
    // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
    // 这里的request是封装好的（上层进行封装），不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service

    public static RPCResponse sendRequest(String host, int port, RPCRequest request) {
        //建立连接
        try {
            Socket  socket = new Socket(host,port);

            //获得输出流和输入流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //对输出流写入请求
            System.out.println(request);
            oos.writeObject(request);
            oos.flush();

            //获取输入流中的Response对象
            RPCResponse response = (RPCResponse)ois.readObject();

            return response;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

}
