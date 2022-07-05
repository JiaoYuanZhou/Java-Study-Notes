import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RPCClient {
    public static void main(String[] args) {
        //建立连接
        try {
            Socket socket = new Socket("localhost",8899);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //传数据给服务端
            RPCRequest request = RPCRequest.builder()
                    .interfaceName("UserService")
                    .methodName("getUserByUserId")
                    .params(new Object[]{1})
                    .paramsTypes(new Class<?>[]{Integer.class}).build();
            oos.writeObject(request);
            oos.flush();

            //接受服务端的数据
            RPCResponse response = (RPCResponse)ois.readObject();
            User user = (User) response.getData();
            System.out.println("服务端传送的User:"+ user);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
